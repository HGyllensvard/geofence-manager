package com.hgyllensvard.geofencemanager.geofence;


import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Singleton
public class SelectedGeofence {

    private SelectedGeofenceId selectedGeofenceId;
    private GeofenceManager geofenceManager;

    @Inject
    public SelectedGeofence(
            SelectedGeofenceId selectedGeofenceId,
            GeofenceManager geofenceManager
    ) {
        this.selectedGeofenceId = selectedGeofenceId;
        this.geofenceManager = geofenceManager;
    }

    public Single<Geofence> selectedGeofence() {
        return Single.fromCallable(() -> selectedGeofenceId.selectedGeofenceId())
                .flatMap(geofenceId -> {
                    if (geofenceId == Geofence.NO_ID) {
                        return Single.just(Geofence.sDummyGeofence);
                    }

                    return geofenceManager.getGeofence(geofenceId);
                });
    }

    public Maybe<Geofence> selectedValidGeofence() {
        return selectedGeofence()
                .filter(geofence -> !geofence.equals(Geofence.sDummyGeofence));
    }

    /**
     * @return Observable that will only emit a new item once a
     * valid existing geofence has been selected.
     */
    public Observable<Geofence> observeValidSelectedGeofence() {
        return observeSelectedGeofence()
                .filter(geofence -> !geofence.equals(Geofence.sDummyGeofence));
    }

    /**
     * @return Observable that returns the selected Geofence.
     * This will include the dummy Geofence returned if there is
     * currently no selected Geofence. This can then be used to know
     * if a Geofence is selected or not.
     */
    public Observable<Geofence> observeSelectedGeofence() {
        return selectedGeofenceId.observeSelectedGeofenceId()
                .flatMap(geofenceId -> {
                    if (geofenceId == Geofence.NO_ID) {
                        return Observable.just(Geofence.sDummyGeofence);
                    }

                    return geofenceManager.getGeofence(geofenceId)
                            .toObservable();

                })
                .distinctUntilChanged();
    }

    public Single<Boolean> delete() {
        if (!selectedGeofenceId.isGeofenceSelected()) {
            return Single.just(false);
        }

        return geofenceManager.removeGeofence(selectedGeofenceId.selectedGeofenceId())
                .doOnSuccess(successfullyDeletedGeofence -> {
                    if (successfullyDeletedGeofence) {
                        selectedGeofenceId.setNoSelection();
                    }
                });
    }
}
