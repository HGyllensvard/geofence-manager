package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

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

    public Single<Boolean> activateGeofence(GeofenceData geofenceData) {
        return Single.just(assembleGeofence(geofenceData.name(), geofenceData.latLng(), radius))
                .flatMap(addPlayGeofenceManager::addGeofence);
    }

    public Single<Boolean> removeGeofence(GeofenceData geofenceData) {
        return removePlayGeofenceManager.removeGeofence(geofenceData.name());
    }

    private Geofence assembleGeofence(String geofenceId, LatLng latLng, double radius) {
        return new Geofence.Builder()
                .setRequestId(geofenceId)
                .setCircularRegion(
                        latLng.latitude,
                        latLng.longitude,
                        (float) radius
                )
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }
}