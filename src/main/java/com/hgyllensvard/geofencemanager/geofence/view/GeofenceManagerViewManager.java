package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.hgyllensvard.geofencemanager.R;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class GeofenceManagerViewManager implements GeofenceManagerView {

    private final AppCompatActivity activity;
    private final SupportMapFragment mapFragment;
    private final Single<Boolean> displayMap;

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
                .subscribeOn(AndroidSchedulers.mainThread());
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
}
