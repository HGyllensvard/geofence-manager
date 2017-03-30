package com.hgyllensvard.geofencemanager.geofence.edit.map;


import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.hgyllensvard.geofencemanager.geofence.edit.map.dragging.DragEvent;
import com.hgyllensvard.geofencemanager.geofence.edit.map.exception.MapNotInitialisedException;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.RequestPermissionResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MapView {

    private final AppCompatActivity activity;
    private final int mapContainer;

    private final SupportMapFragment mapFragment;
    private final LocationManager locationManager;
    private final GeofenceViewMapsManager geofenceMapManagers;

    private Observable<LatLng> longClickObservable;
    private Observable<Long> selectMarkerObservable;
    private Observable<Integer> cameraMovedObservable;
    private Observable<Boolean> mapReadyObservable;
    private Observable<DragEvent> markerDraggedObservable;

    private GoogleMap googleMap;
    private CompositeDisposable disposables;

    public MapView(
            AppCompatActivity activity,
            @IdRes int mapContainer,
            LocationManager locationManager,
            GeofenceViewMapsManager geofenceMapManagers
    ) {
        this.activity = activity;
        this.mapContainer = mapContainer;
        this.locationManager = locationManager;
        this.geofenceMapManagers = geofenceMapManagers;

        disposables = new CompositeDisposable();
        mapFragment = SupportMapFragment.newInstance();
        mapReadyObservable = createMapReadyObservable(activity);
    }

    public Observable<Boolean> initialiseAndDisplayMap() {
        return mapReadyObservable;
    }

    private void enableUserLocation() throws SecurityException {
        googleMap.setMyLocationEnabled(true);
    }

    public void animateCameraTo(CameraUpdate cameraUpdate) {
        googleMap.animateCamera(cameraUpdate);
    }

    public Observable<LatLng> observeLongClick() {
        if (longClickObservable == null) {
            throw new MapNotInitialisedException();
        }

        return longClickObservable;
    }

    public Observable<DragEvent> observeMarkerDragged() {
        if (markerDraggedObservable == null) {
            throw new MapNotInitialisedException();
        }

        return markerDraggedObservable;
    }

    public Observable<Long> observeGeofenceSelected() {
        if (selectMarkerObservable == null) {
            throw new MapNotInitialisedException();
        }

        return selectMarkerObservable;
    }

    public Observable<Integer> observeCameraStartMoving() {
        if (cameraMovedObservable == null) {
            throw new MapNotInitialisedException();
        }

        return cameraMovedObservable;
    }

    private Observable<Boolean> createMapReadyObservable(AppCompatActivity activity) {
        return checkOrRequestLocationPermission()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> activity.finish())
                .flatMap(ignored -> loadMapAsync())
                .doOnDispose(() -> {
                    if (!activity.isFinishing()) {
                        Timber.d("Removing map fragment");
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .remove(mapFragment)
                                .commit();
                    }

                    disposables.clear();
                })
                .doOnNext(ignored -> {
                    googleMap.getUiSettings().setMapToolbarEnabled(false);
                    enableUserLocation();
                    longClickObservable = createLongPressMapObservable()
                            .subscribeOn(AndroidSchedulers.mainThread());
                    selectMarkerObservable = createSelectedGeofenceObservable()
                            .subscribeOn(AndroidSchedulers.mainThread());
                    cameraMovedObservable = createCameraMoveStartedObservable()
                            .subscribeOn(AndroidSchedulers.mainThread());
                    markerDraggedObservable = createDraggableObservable()
                            .subscribeOn(AndroidSchedulers.mainThread());

                    subscribeMarkerDragged();
                    Timber.i("Map View successfully setup");
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .replay(1)
                .refCount();
    }

    private void subscribeMarkerDragged() {
        Disposable disposable = observeMarkerDragged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(dragEvent -> {
                            GeofenceViewMapManager viewMapManager = geofenceMapManagers.get(dragEvent.geofence().id());
                            viewMapManager.updatePosition(dragEvent.newPosition());
                        },
                        Timber::e);

        disposables.add(disposable);
    }

    private Observable<RequestPermissionResult> checkOrRequestLocationPermission() {
        return locationManager.request()
                .map(requestPermissionResult -> {
                    if (requestPermissionResult == RequestPermissionResult.DENIED) {
                        throw new IllegalStateException("Trying to display map view, but no location permission");
                    }

                    return requestPermissionResult;
                }).toObservable();
    }

    public void removedGeofenceViews(List<GeofenceView> geofenceViews) {
        for (GeofenceView geofenceView : geofenceViews) {
            GeofenceViewMapManager viewMapManager = geofenceMapManagers.remove(geofenceView.id());
            if (viewMapManager != null) {
                viewMapManager.remove();
            }
        }
    }

    @Nullable
    public LatLng getGeofencePosition(long geofenceId) {
        GeofenceViewMapManager mapManager = geofenceMapManagers.get(geofenceId);

        if (mapManager == null) {
            return null;
        }

        return mapManager.position();
    }

    public void updateGeofenceViews(List<GeofenceView> geofenceViews) {
        for (GeofenceView geofenceView : geofenceViews) {
            GeofenceViewMapManager viewMapManager = updateGeofenceViewMapManager(geofenceView);
            viewMapManager.display(googleMap);
        }
    }

    @NonNull
    private GeofenceViewMapManager updateGeofenceViewMapManager(GeofenceView geofenceView) {
        GeofenceViewMapManager viewMapManager = new GeofenceViewMapManager(geofenceView);
        geofenceMapManagers.put(geofenceView.id(), viewMapManager);
        return viewMapManager;
    }

    private Observable<Boolean> loadMapAsync() {
        return Observable.create(e -> {
            addMapFragment();

            Timber.d("Fetching map asynchronously");
            mapFragment.getMapAsync(map -> {
                Timber.i("Map fetched");
                googleMap = map;
                e.onNext(true);
            });
        });
    }

    private void addMapFragment() {
        Timber.d("adding map fragment to container");

        FragmentTransaction fragmentTransaction = activity
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.add(mapContainer, mapFragment);
        fragmentTransaction.commit();
        Timber.v("Map committed to be displayed");
    }

    private Observable<LatLng> createLongPressMapObservable() {
        return Observable.create(emitter -> {
            googleMap.setOnMapLongClickListener(emitter::onNext);
            emitter.setCancellable(() -> activity.runOnUiThread(() -> googleMap.setOnMapLongClickListener(null)));
        });
    }

    private Observable<Long> createSelectedGeofenceObservable() {
        return Observable.create(emitter -> {
            googleMap.setOnMarkerClickListener(marker -> {
                emitter.onNext(geofenceMapManagers.findGeofenceId(marker.getId()));

                // Not the most effective solution, but I rather take implementation elegance here
                return false;
            });

            emitter.setCancellable(() -> googleMap.setOnMarkerClickListener(null));
        });
    }

    private Observable<Integer> createCameraMoveStartedObservable() {
        return Observable.create(emitter -> {
            googleMap.setOnCameraMoveStartedListener(emitter::onNext);
            emitter.setCancellable(() -> googleMap.setOnCameraMoveStartedListener(null));
        });
    }

    private Observable<DragEvent> createDraggableObservable() {
        return Observable.create(emitter -> {
            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    Timber.v("Dragging started");
                    emitter.onNext(DragEvent.dragStarted(findGeofence(marker), marker.getPosition()));
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    emitter.onNext(DragEvent.dragging(findGeofence(marker), marker.getPosition()));
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Timber.v("Dragging ended");
                    emitter.onNext(DragEvent.draggingEnded(findGeofence(marker), marker.getPosition()));
                }
            });

            emitter.setCancellable(() -> googleMap.setOnMarkerDragListener(null));
        });
    }

    private Geofence findGeofence(Marker marker) {
        GeofenceViewMapManager mapManager = geofenceMapManagers.findGeofenceViewMapManager(marker.getId());

        if (mapManager == null) {
            throw new IllegalArgumentException(String.format("Marker: %s does not have a corresponding GeofenceViewMapManager which is should have.", marker));
        }

        return mapManager.getGeofence();
    }
}
