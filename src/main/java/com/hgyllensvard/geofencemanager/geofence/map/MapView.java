package com.hgyllensvard.geofencemanager.geofence.map;


import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.Geofence;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MapView {

    private final AppCompatActivity activity;
    private final int mapContainer;

    private final SupportMapFragment mapFragment;
    private final GeofenceViewManager geofenceViewManager;

    private Flowable<LatLng> longClickFlowable;
    private Flowable<Long> selectMarkerFlowable;
    private Flowable<Integer> cameraMovedFlowable;
    private Flowable<Boolean> mapFlowable;

    private GoogleMap googleMap;
    private CompositeDisposable disposables;

    public MapView(
            AppCompatActivity activity,
            @IdRes int mapContainer,
            GeofenceViewManager geofenceViewManager
    ) {
        this.activity = activity;
        this.mapContainer = mapContainer;
        this.geofenceViewManager = geofenceViewManager;

        disposables = new CompositeDisposable();
        mapFragment = SupportMapFragment.newInstance();
        mapFlowable = createMapFlowable(activity);
    }

    public Flowable<Boolean> initialiseAndDisplayMap() {
        return mapFlowable;
    }

    private void enableUserLocation() throws SecurityException {
        googleMap.setMyLocationEnabled(true);
    }

    public void displayGeofences(List<Geofence> geofences) {
        geofenceViewManager.updateGeofenceViews(geofences);
    }

    public void animateCameraTo(CameraUpdate cameraUpdate) {
        googleMap.animateCamera(cameraUpdate);
    }

    public Flowable<LatLng> observerLongClick() {
        if (longClickFlowable == null) {
            throw new MapNotInitialisedError();
        }

        return longClickFlowable;
    }

    public Flowable<Long> observeGeofenceSelected() {
        if (selectMarkerFlowable == null) {
            throw new MapNotInitialisedError();
        }

        return selectMarkerFlowable;
    }

    public Flowable<Integer> observeCameraStartMoving() {
        if (cameraMovedFlowable == null) {
            throw new MapNotInitialisedError();
        }

        return cameraMovedFlowable;
    }

    private Flowable<Boolean> createMapFlowable(AppCompatActivity activity) {
        return loadMapAsync()
                .doOnTerminate(() -> {
                    Timber.d("Removing map fragment");
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .remove(mapFragment)
                            .commit();
                })
                .doOnNext(ignored -> {
                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                    enableUserLocation();
                    longClickFlowable = createLongPressMapFlowable();
                    selectMarkerFlowable = createSelectedGeofenceFlowable();
                    cameraMovedFlowable = createCameraMoveStartedFlowable();
                    createUpdatedGeofenceViewsListener();
                    createRemovedGeofenceViewsListener();
                    Timber.i("Map View successfully setup");
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .share();
    }

    private void createRemovedGeofenceViewsListener() {
        Disposable disposable = geofenceViewManager.observeRemovedGeofenceViews()
                .filter(geofenceViews -> !geofenceViews.isEmpty())
                .flatMap(Observable::fromIterable)
                .subscribe(GeofenceView::delete,
                        Timber::e);

        disposables.add(disposable);
    }

    private void createUpdatedGeofenceViewsListener() {
        Disposable disposable = geofenceViewManager.observeUpdatedGeofenceViews()
                .filter(geofenceViews -> !geofenceViews.isEmpty())
                .flatMap(Observable::fromIterable)
                .subscribe(geofenceView -> geofenceView.display(googleMap),
                        Timber::e);

        disposables.add(disposable);
    }

    private Flowable<Boolean> loadMapAsync() {
        return Flowable.create(e -> {
            addMapFragment();

            Timber.d("Fetching map asynchronously");
            mapFragment.getMapAsync(map -> {
                Timber.i("Map fetched");
                googleMap = map;
                e.onNext(true);
            });
        }, BackpressureStrategy.BUFFER);
    }

    private void addMapFragment() {
        Timber.d("adding map fragment to container");

        FragmentTransaction fragmentTransaction = activity
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.add(mapContainer, mapFragment);
        fragmentTransaction.commit();
    }

    private Flowable<LatLng> createLongPressMapFlowable() {
        return Flowable.create(emitter -> {
            googleMap.setOnMapLongClickListener(emitter::onNext);
            emitter.setCancellable(() -> googleMap.setOnMapLongClickListener(null));
        }, BackpressureStrategy.BUFFER);
    }

    private Flowable<Long> createSelectedGeofenceFlowable() {
        return Flowable.create(emitter -> {
            googleMap.setOnMarkerClickListener(marker -> {
                emitter.onNext(geofenceViewManager.findGeofenceId(marker.getId()));

                // Not the most effective solution, but I rather take implementation elegance here
                return false;
            });

            emitter.setCancellable(() -> googleMap.setOnMarkerClickListener(null));
        }, BackpressureStrategy.BUFFER);
    }

    private Flowable<Integer> createCameraMoveStartedFlowable() {
        return Flowable.create(emitter -> {
            googleMap.setOnCameraMoveStartedListener(emitter::onNext);
            emitter.setCancellable(() -> googleMap.setOnCameraMoveStartedListener(null));
        }, BackpressureStrategy.BUFFER);
    }
}
