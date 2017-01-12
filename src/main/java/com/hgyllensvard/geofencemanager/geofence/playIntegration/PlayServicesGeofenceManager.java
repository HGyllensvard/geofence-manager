package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.Geofence;

import io.reactivex.Single;

import static android.R.attr.radius;

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

    public Single<Boolean> activateGeofence(Geofence geofence) {
        return Single.just(assembleGeofence(geofence.name(), geofence.latLng(), radius))
                .flatMap(addPlayGeofenceManager::addGeofence);
    }

    public Single<Boolean> removeGeofence(Geofence geofence) {
        return removePlayGeofenceManager.removeGeofence(geofence.name());
    }

    private com.google.android.gms.location.Geofence assembleGeofence(String geofenceId, LatLng latLng, double radius) {
        return new com.google.android.gms.location.Geofence.Builder()
                .setRequestId(geofenceId)
                .setCircularRegion(
                        latLng.latitude,
                        latLng.longitude,
                        (float) radius
                )
                .setExpirationDuration(com.google.android.gms.location.Geofence.NEVER_EXPIRE)
                .setTransitionTypes(com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER | com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }
}