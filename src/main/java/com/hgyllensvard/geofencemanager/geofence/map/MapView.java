package com.hgyllensvard.geofencemanager.geofence.map;


import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MapView {

    private final AppCompatActivity activity;
    private final int mapContainer;

    private final SupportMapFragment mapFragment;

    private Flowable<LatLng> longClickFlowable;
    private Flowable<Marker> selectMarkerFlowable;
    private Flowable<Integer> cameraMovedFlowable;
    private Flowable<Boolean> mapFlowable;

    private GoogleMap googleMap;

    public MapView(
            AppCompatActivity activity,
            @IdRes int mapContainer
    ) {
        this.activity = activity;
        this.mapContainer = mapContainer;

        mapFragment = SupportMapFragment.newInstance();
        mapFlowable = createMapFlowable(activity);
    }

    public Flowable<Boolean> initialiseAndDisplayMap() {
        return mapFlowable;
    }

    private void enableUserLocation() throws SecurityException {
        googleMap.setMyLocationEnabled(true);
    }

    public void displayMarkers(List<GeofenceMarker> markers) {
        for (GeofenceMarker marker : markers) {
            marker.display(googleMap);
        }
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

    public Flowable<Marker> observeMarkerSelected() {
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
                .doOnNext(ignored -> enableUserLocation())
                .doOnNext(ignored -> longClickFlowable = createLongPressMapFlowable())
                .doOnNext(ignored -> selectMarkerFlowable = createSelectedMarkerFlowable())
                .doOnNext(ignored -> cameraMovedFlowable = createCameraMoveStartedFlowable())
                .doOnNext(ignored -> Timber.i("Map View successfully setup"))
                .subscribeOn(AndroidSchedulers.mainThread())
                .share();
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

    private Flowable<Marker> createSelectedMarkerFlowable() {
        return Flowable.create(emitter -> {
            googleMap.setOnMarkerClickListener(marker -> {
                emitter.onNext(marker);

                // Not the most effective solution, but I rather take implementation elegance here
                return false;
            });

//            emitter.setCancellable(() -> activity.runOnUiThread(() -> googleMap.setOnMarkerClickListener(null)));
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
