package com.hgyllensvard.geofencemanager.geofence.selectedGeofence;


import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

@Singleton
public class SelectedGeofenceId {

    private BehaviorSubject<SelectedGeofenceIdState> subject;

    public SelectedGeofenceId() {
        subject = BehaviorSubject.createDefault(SelectedGeofenceIdState.noSelection());
    }

    public synchronized SelectedGeofenceIdState selectedGeofenceState() {
        return subject.getValue();
    }

    public synchronized long selectedGeofenceId() {
        return subject.getValue().geofenceId();
    }

    /**
     * @return Observable returning the currently selected id.
     * This will always return a valid id, it does not guarantee that a
     * geofence exist with that id.
     * <p>
     * If you're only interested in the currently selected Geofence, see {@link SelectedGeofence}
     */
    public Observable<SelectedGeofenceIdState> observeValidSelectedGeofenceId() {
        return observeSelectedGeofenceId()
                .doOnEach(selectedGeofenceIdStateNotification -> Timber.wtf("Cool A: %s", selectedGeofenceIdStateNotification))
                .filter(SelectedGeofenceIdState::isGeofenceSelected)
                .doOnEach(selectedGeofenceIdStateNotification -> Timber.wtf("Cool B: %s", selectedGeofenceIdStateNotification));
    }

    /**
     * @return Observable returning the id of the current {@link Geofence} selected.
     * If no Geofence is selected a no_id is set to notify of this change.
     */
    public Observable<SelectedGeofenceIdState> observeSelectedGeofenceId() {
        return subject
                .distinctUntilChanged();
    }

    public synchronized boolean isGeofenceSelected() {
        return subject.getValue().isGeofenceSelected();
    }

    public synchronized void setNoSelection() {
        Timber.d("No selected Geofence");
        subject.onNext(SelectedGeofenceIdState.noSelection());
    }

    public synchronized boolean selectedGeofence(long geofenceId) {
        if (isInvalidGeofenceId(geofenceId)) {
            return false;
        }

        Timber.d("New Geofence selected: %s", geofenceId);
        if (geofenceId == Geofence.NO_ID) {
            setNoSelection();
        } else {
            subject.onNext(SelectedGeofenceIdState.selected(geofenceId));
        }

        return true;
    }

    private boolean isInvalidGeofenceId(long geofenceId) {
        return geofenceId <= 0 && geofenceId != Geofence.NO_ID;
    }
}
