package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class GeofenceManagerViewManager implements GeofenceManagerView {

    private final AppCompatActivity activity;
    private final SupportMapFragment mapFragment;
    private final Single<Boolean> displayMap;

    private Flowable<LatLng> longClickFlowable;
    private GoogleMap googleMap;

    public GeofenceManagerViewManager(AppCompatActivity activity) {
        this.activity = activity;

        mapFragment = SupportMapFragment.newInstance();

        displayMap = Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> e) throws Exception {
                addMapFragment();

                mapFragment.getMapAsync(map -> {
                    googleMap = map;
                    longClickFlowable = createLongPressEmitter();
                    e.onSuccess(true);
                });
            }
        });
    }

    @Override
    public Single<Boolean> displayMap() {
        return displayMap
                .doOnDispose(() ->
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .remove(mapFragment)
                                .commit())
                .doOnSuccess(disposable -> {
                    try {
                        googleMap.setMyLocationEnabled(true);
                    } catch (SecurityException e) {
                        Timber.e(e, "Unexpected error");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void animateCameraTo(CameraUpdate cameraUpdate) {
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void addGeofence(GeofenceData geofenceData) {
        googleMap.addMarker(new MarkerOptions()
                .position(geofenceData.latLng())
                .draggable(true));
    }

    /**
     * Will be null until the displayMap has returned
     */
    @Override
    @Nullable
    public Flowable<LatLng> observerLongClick() {
        return longClickFlowable;
    }

    @Override
    public void displayGeofences(List<GeofenceData> geofenceDatas) {
        
    }

    @Override
    public void destroy() {

    }

    private void addMapFragment() {
        FragmentTransaction fragmentTransaction = activity
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.add(R.id.geofence_map_container, mapFragment);
        fragmentTransaction.commit();
    }

    private Flowable<LatLng> createLongPressEmitter() {
        return Flowable.create((FlowableOnSubscribe<LatLng>) emitter -> {
            googleMap.setOnMapLongClickListener(emitter::onNext);
            emitter.setCancellable(() -> googleMap.setOnMapLongClickListener(null));
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
