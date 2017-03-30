package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EditGeofencePresenterManager {

    private GeofenceManager geofenceManager;
    private SelectedGeofence selectedGeofence;

    @Inject
    public EditGeofencePresenterManager(
            GeofenceManager geofenceManager,
            SelectedGeofence selectedGeofence
    ) {
        this.geofenceManager = geofenceManager;
        this.selectedGeofence = selectedGeofence;
    }

    /**
     * @return Will only return a valid Geofences,
     * if selected geofence is invalid no geofence is returned.
     */
    Observable<Geofence> observeSelectedGeofence() {
        return selectedGeofence.observeSelectedGeofence()
                .filter(this::isValidGeofenceId)
                .flatMap(geofenceId -> geofenceManager.getGeofence(geofenceId)
                        .toObservable())
                .subscribeOn(Schedulers.trampoline());
    }

    void updateSelectedGeofence(
            String geofenceName,
            @Nullable LatLng geofencePosition
    ) {
        Maybe.fromCallable(selectedGeofence::selectedGeofence)
                .doOnSuccess(geofenceId -> Timber.v("Updating geofence with Id: %s to position: %s ", geofenceId, geofencePosition))
                .filter(this::isValidGeofenceId)
                .flatMap(geofence -> geofenceManager.getGeofence(geofence)
                        .toMaybe())
                .map(geofence -> constructNewGeofence(geofenceName, geofencePosition, geofence))
                .flatMap(geofence -> geofenceManager.updateGeofence(geofence)
                        .toMaybe())
                .subscribeOn(Schedulers.io())
                .subscribe(actionResult -> {
                        }
                        , Timber::e);
    }

    private Geofence constructNewGeofence(String geofenceName, @Nullable LatLng geofencePosition, Geofence geofence) {
        Geofence tempGeofence = geofence.withName(geofenceName);

        if (geofencePosition == null) {
            return tempGeofence;
        } else {
            return tempGeofence.withLatLng(geofencePosition);
        }
    }

    Single<Boolean> deleteSelectedGeofence() {
        long geofenceId = selectedGeofence.selectedGeofence();
        if (!isValidGeofenceId(geofenceId)) {
            return Single.just(false);
        }

        return geofenceManager.removeGeofence(geofenceId)
                .observeOn(Schedulers.io())
                .doOnSuccess(successfullyDeletedGeofence -> {
                    if (successfullyDeletedGeofence) {
                        selectedGeofence.setNoSelection();
                    }
                });
    }

    private boolean isValidGeofenceId(Long geofenceId) {
        return geofenceId != Geofence.NO_ID;
    }
}
