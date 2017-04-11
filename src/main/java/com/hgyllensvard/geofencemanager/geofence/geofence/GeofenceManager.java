package com.hgyllensvard.geofencemanager.geofence.geofence;


import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.persistence.exceptions.InsertFailedException;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Class is responsible for managing the connection to
 * Google Play Services Geofence API and it will then also
 * persist the Geofences that exist.
 */
public class GeofenceManager {

    private final GeofenceRepository geofenceRepository;
    private final PlayServicesGeofenceManager playServicesGeofenceManager;

    @Inject
    public GeofenceManager(
            GeofenceRepository geofenceRepository,
            PlayServicesGeofenceManager playServicesGeofenceManager
    ) {
        this.geofenceRepository = geofenceRepository;
        this.playServicesGeofenceManager = playServicesGeofenceManager;
    }

    /**
     * @param geofenceToAdd Geofence to persist and add to the enabled geofences.
     * @return The GeofenceActionResult will hld a boolean for success or not.
     * If success is true the result will also contain the Geofence with the
     * database id.
     * <p>
     * Upon a failure success is false and the related Error can be fetched
     * from the failure method.
     */
    public Single<GeofenceActionResult> addGeofence(Geofence geofenceToAdd) {
        return geofenceRepository.insert(geofenceToAdd)
                .flatMap(geofence -> {
                    if (geofence.equals(Geofence.sDummyGeofence)) {
                        return Single.just(GeofenceActionResult.failure(new InsertFailedException(geofence.toString())));
                    }

                    return activateGeofenceToPlay(geofence);
                })
                .doOnSuccess(geofenceActionResult -> Timber.v("Add geofence result: %s", geofenceActionResult))
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> removeGeofence(long geofenceId) {
        return playServicesGeofenceManager.removeGeofence(geofenceId)
                .flatMap(ignored -> geofenceRepository.delete(geofenceId))
                .onErrorReturnItem(false)
                .subscribeOn(Schedulers.io());
    }

    public Single<GeofenceActionResult> updateGeofence(Geofence geofence) {
        Timber.v("Updating Geofence with id: %s to: %s", geofence.id(), geofence);
        return Single.fromCallable(() -> playServicesGeofenceManager.removeGeofence(geofence.id()))
                .flatMap(ignored -> geofenceRepository.update(geofence))
                .flatMap(successfullyUpdatedGeofence -> {
                    if (successfullyUpdatedGeofence) {
                        if (geofence.enabled()) {
                            return activateGeofenceToPlay(geofence);
                        } else {
                            return Single.just(GeofenceActionResult.success(geofence));
                        }
                    } else {
                        return Single.just(GeofenceActionResult.failure(new FailedToUpdateGeofenceException(geofence)));
                    }
                });
    }

    public Flowable<List<Geofence>> observeGeofences() {
        return geofenceRepository.listenGeofences();
    }

    public Single<Boolean> exist(long geofenceId) {
        Timber.v("Checking if Geofence with id: %s exists", geofenceId);
        return geofenceRepository.getGeofence(geofenceId)
                .map(GeofenceResult::success);
    }

    public Single<GeofenceResult> getGeofence(long identifier) {
        return geofenceRepository.getGeofence(identifier);
    }

    private Single<GeofenceActionResult> activateGeofenceToPlay(Geofence addedGeofence) {
        return playServicesGeofenceManager.activateGeofence(addedGeofence)
                .flatMap(successfullyActivatedGeofence -> {
                    if (successfullyActivatedGeofence) {
                        return Single.just(GeofenceActionResult.success(addedGeofence));
                    } else {
                        return manageFailedToAddGeofence(addedGeofence);
                    }
                });
    }

    /*
     * Try to clean up the database if the geofence couldn't be added to the play services.
     */
    private SingleSource<? extends GeofenceActionResult> manageFailedToAddGeofence(Geofence geofence) {
        return geofenceRepository.delete(geofence.id())
                .map(deleted -> GeofenceActionResult.failure(new FailedToAddGeofenceException()));
    }
}
