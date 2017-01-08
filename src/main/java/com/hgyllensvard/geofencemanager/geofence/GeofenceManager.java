package com.hgyllensvard.geofencemanager.geofence;


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

    public Single<GeofenceData> addGeofence(
            GeofenceData geofenceData
    ) {
        return playServicesGeofenceManager.activateGeofence(geofenceData)
                .flatMap(successfullyActivatedGeofence -> geofenceRepository.insert(geofenceData))
                .map(successfullyPersistedGeofence -> geofenceData)
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> removeGeofence(GeofenceData geofenceData) {
        return playServicesGeofenceManager.removeGeofence(geofenceData)
                .flatMap(aBoolean -> geofenceRepository.delete(geofenceData)).subscribeOn(Schedulers.io());
    }

    public Single<GeofenceData> updateGeofence(GeofenceData oldGeofenceData, GeofenceData updatedGeofenceData) {
        return removeGeofence(oldGeofenceData)
                .flatMap(successfullyRemoved -> addGeofence(updatedGeofenceData));
    }

    public Flowable<List<GeofenceData>> observeGeofences() {
        return geofenceRepository.listenGeofences();
    }
}
