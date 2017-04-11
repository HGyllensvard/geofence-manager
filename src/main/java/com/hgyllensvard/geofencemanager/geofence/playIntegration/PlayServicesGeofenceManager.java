package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import io.reactivex.Single;
import timber.log.Timber;

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
        return Single.just(assembleGeofence(geofence.id(), geofence.latLng(), radius))
                .flatMap(addPlayGeofenceManager::addGeofence);
    }

    public Single<Boolean> removeGeofence(long geofenceId) {
        return removePlayGeofenceManager.removeGeofence(String.valueOf(geofenceId))
                .doOnEvent((successfullyRemoved, throwable) -> {
                    if (successfullyRemoved) {
                        Timber.i("Successfully removed geofence with id: %s", geofenceId);
                    } else {
                        Timber.w(throwable, "Failed to delete geofence with id: %s", geofenceId);
                    }
                });
    }

    private com.google.android.gms.location.Geofence assembleGeofence(long geofenceId, LatLng latLng, double radius) {
        return new com.google.android.gms.location.Geofence.Builder()
                .setRequestId(String.valueOf(geofenceId))
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