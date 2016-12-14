package com.hgyllensvard.geofencemanager.geofence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;

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

    public Single<GeofenceData> addGeofence(String name, LatLng latLng) {
        return playServicesGeofenceManager.activateGeofence(name, latLng)
                .flatMap(successfullyActivatedGeofence -> geofenceRepository.save(name, latLng))
                .map(successfullyPersistedGeofence -> GeofenceData.create(name, latLng));
    }

    public Single<Boolean> removeGeofence(String name) {
        return null;
    }
}
