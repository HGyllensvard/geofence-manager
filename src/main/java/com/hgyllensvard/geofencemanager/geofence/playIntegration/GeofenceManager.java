package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceModel;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class GeofenceManager {

    private final GeofenceRepository geofenceRepository;
    private final PlayGeofenceManager playGeofenceManager;

    public GeofenceManager(
            GeofenceRepository geofenceRepository,
            PlayGeofenceManager playGeofenceManager
    ) {
        this.geofenceRepository = geofenceRepository;
        this.playGeofenceManager = playGeofenceManager;
    }

    public Single<LatLng> addGeofence(String name, LatLng latLng) {
        playGeofenceManager.addGeofence(name, latLng);
//        TODO, how to add and save a geofence?
        return null;
    }

    public Single<Boolean> removeGeofence(String name) {
        return null;
    }
}
