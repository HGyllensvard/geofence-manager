package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Single;

public class PlayServicesGeofenceManager {

    private final PlayApiManager playApiManager;
    private final AddPlayGeofenceManager addPlayGeofenceManager;
    private final RemovePlayGeofenceManager removePlayGeofenceManager;

    public PlayServicesGeofenceManager(
            PlayApiManager playApiManager,
            AddPlayGeofenceManager activatePlayGeofenceManager,
            RemovePlayGeofenceManager removePlayGeofenceManager
    ) {
        this.playApiManager = playApiManager;
        this.addPlayGeofenceManager = activatePlayGeofenceManager;
        this.removePlayGeofenceManager = removePlayGeofenceManager;
    }

    public Single<Boolean> activateGeofence(String name, LatLng latLng) {
        return playApiManager.connectToPlayServices()
                .flatMap(googleApiClient -> Single.just(assembleGeofence(name, latLng, 100))
                        .flatMap(geofence -> addPlayGeofenceManager.addGeofence(geofence, googleApiClient)));
    }

    public Single<Boolean> removeGeofence(String name) {
        return playApiManager.connectToPlayServices()
                .flatMap(googleApiClient -> removePlayGeofenceManager.removeGeofence(googleApiClient, name));
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