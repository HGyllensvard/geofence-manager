package com.hgyllensvard.geofencemanager.geofence.selectedGeofence;


import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceState.NO_GEOFENCE_SELECTED;

@Singleton
public class SelectedGeofence {

    private SelectedGeofenceId selectedGeofenceId;
    private GeofenceManager geofenceManager;

    private Observable<SelectedGeofenceState> geofenceStateObservable;

    @Inject
    public SelectedGeofence(
            SelectedGeofenceId selectedGeofenceId,
            GeofenceManager geofenceManager
    ) {
        this.selectedGeofenceId = selectedGeofenceId;
        this.geofenceManager = geofenceManager;

        geofenceStateObservable = Observable.defer(() -> selectedGeofenceId.observeSelectedGeofenceId()
                .flatMap(selectedGeofenceState -> {
                    if (!selectedGeofenceState.isGeofenceSelected()) {
                        return Observable.just(NO_GEOFENCE_SELECTED);
                    }

                    return geofenceManager.getGeofence(selectedGeofenceState.geofenceId())
                            .toObservable()
                            .map(geofenceResult -> {
                                if (!geofenceResult.success()) {
                                    return NO_GEOFENCE_SELECTED;
                                }

                                return SelectedGeofenceState.selectedGeofence(geofenceResult.geofence());
                            });
                }))
                .replay(1)
                .refCount();
    }

    public Single<SelectedGeofenceState> selectedGeofence() {
        return geofenceStateObservable
                .take(1)
                .single(NO_GEOFENCE_SELECTED);
    }

    public Maybe<Geofence> selectedValidGeofence() {
        return selectedGeofence()
                .filter(SelectedGeofenceState::validGeofence)
                .map(SelectedGeofenceState::geofence);
    }

    /**
     * @return Observable that returns the selected Geofence.
     * This will include the dummy Geofence returned if there is
     * currently no selected Geofence. This can then be used to know
     * if a Geofence is selected or not.
     */
    public Observable<SelectedGeofenceState> observeSelectedGeofence() {
        return geofenceStateObservable;
    }

    /**
     * @return Observable that will only emit a new item once a
     * valid existing geofence has been selected.
     */
    public Observable<Geofence> observeValidSelectedGeofence() {
        return observeSelectedGeofence()
                .filter(SelectedGeofenceState::validGeofence)
                .map(SelectedGeofenceState::geofence);
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
