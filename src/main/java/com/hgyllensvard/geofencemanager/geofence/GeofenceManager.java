package com.hgyllensvard.geofencemanager.geofence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;


/**
 * Class is responsible for managing the connection to
 * Gogole Play Services Geofence API and it will then also
 * persist the Geofences that exist.
 */
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
                .map(successfullyPersistedGeofence -> GeofenceData.create(name, latLng))
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> removeGeofence(String name) {
        return playServicesGeofenceManager.removeGeofence(name)
                .flatMap(aBoolean -> geofenceRepository.delete(name)).subscribeOn(Schedulers.io());
    }

    public Single<GeofenceData> updateGeofence(String name, LatLng newLatlng) {
        return removeGeofence(name)
                .flatMap(successfullyRemoved -> addGeofence(name, newLatlng));
    }

    public Flowable<List<GeofenceData>> observeGeofences() {
        return geofenceRepository.listenGeofences();
    }
}
