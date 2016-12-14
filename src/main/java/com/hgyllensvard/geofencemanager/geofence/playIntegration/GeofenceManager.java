package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;

import io.reactivex.Single;

public class GeofenceManager {

    private final GeofenceRepository geofenceRepository;
    private final PlayServicesGeofenceManager playServicesGeofenceManager;

    public GeofenceManager(
            GeofenceRepository geofenceRepository,
            PlayServicesGeofenceManager playServicesGeofenceManager
    ) {
        this.geofenceRepository = geofenceRepository;
        this.playServicesGeofenceManager = playServicesGeofenceManager;
    }

    public Single<Boolean> addGeofence(String name, LatLng latLng) {
        return playServicesGeofenceManager.activateGeofence(name, latLng)
                .flatMap(aBoolean -> geofenceRepository.save(name, latLng));
    }

    public Single<Boolean> removeGeofence(String name) {
        return null;
    }
}
