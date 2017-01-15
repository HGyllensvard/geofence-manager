package com.hgyllensvard.geofencemanager.geofence;


import android.support.annotation.NonNull;

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

    public Single<Geofence> addGeofence(
            Geofence geofence
    ) {
        return playServicesGeofenceManager.activateGeofence(geofence)
                .flatMap(successfullyActivatedGeofence -> geofenceRepository.insert(geofence))
                .map(successfullyPersistedGeofence -> geofence)
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> removeGeofence(long geofenceId) {
        return playServicesGeofenceManager.removeGeofence(geofenceId)
                .flatMap(aBoolean -> geofenceRepository.delete(geofenceId)).subscribeOn(Schedulers.io());
    }

    public Single<Geofence> updateGeofence(Geofence oldGeofence, Geofence updatedGeofence) {
        return Single.fromCallable(() -> validateUpdateGeofenceInput(oldGeofence, updatedGeofence))
                .flatMap(ignored -> playServicesGeofenceManager.removeGeofence(oldGeofence.id()))
                .flatMap(ignored -> playServicesGeofenceManager.activateGeofence(updatedGeofence))
                .flatMap(successfullyRemoved -> geofenceRepository.update(updatedGeofence))
                .map(aBoolean -> updatedGeofence);
    }

    @NonNull
    private Boolean validateUpdateGeofenceInput(Geofence oldGeofence, Geofence updatedGeofence) {
        if (oldGeofence.id() != updatedGeofence.id()) {
            throw new IllegalArgumentException("Not updating the same geofence, old: " + oldGeofence + ", new: " + updatedGeofence);
        }

        return true;
    }

    public Flowable<List<Geofence>> observeGeofences() {
        return geofenceRepository.listenGeofences();
    }

    public Single<Geofence> getGeofence(long identifier) {
        return geofenceRepository.getGeofence(identifier);
    }
}
