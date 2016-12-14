package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;

public class PlayServicesGeofenceManager {

    private final PlayApiManager playApiManager;
    private final ActivateGeofenceManager activateGeofenceManager;

    public PlayServicesGeofenceManager(
            PlayApiManager playApiManager,
            ActivateGeofenceManager activateGeofenceManager
    ) {
        this.playApiManager = playApiManager;
        this.activateGeofenceManager = activateGeofenceManager;
    }

    public Single<Boolean> activateGeofence(String name, LatLng latLng) {
        return playApiManager.connectToPlayServices()
                .flatMap(googleApiClient -> Single.just(assembleGeofence(name, latLng, 100))
                        .flatMap(geofence -> activateGeofenceManager.activateGeofence(geofence, googleApiClient)));
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
