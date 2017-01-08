package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;

public class PlayServicesGeofenceManager {

    private final AddPlayGeofenceManager addPlayGeofenceManager;
    private final RemovePlayGeofenceManager removePlayGeofenceManager;

    public PlayServicesGeofenceManager(
            AddPlayGeofenceManager activatePlayGeofenceManager,
            RemovePlayGeofenceManager removePlayGeofenceManager
    ) {
        this.addPlayGeofenceManager = activatePlayGeofenceManager;
        this.removePlayGeofenceManager = removePlayGeofenceManager;
    }

    public Single<Boolean> activateGeofence(String name, LatLng latLng) {
        return Single.just(assembleGeofence(name, latLng, 100))
                .flatMap(addPlayGeofenceManager::addGeofence);
    }

    public Single<Boolean> removeGeofence(String name) {
        return removePlayGeofenceManager.removeGeofence(name);
    }

    private Geofence assembleGeofence(String geofenceId, LatLng latLng, float radius) {
        return new Geofence.Builder()
                .setRequestId(geofenceId)
                .setCircularRegion(
                        latLng.latitude,
                        latLng.longitude,
                        radius
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }
}