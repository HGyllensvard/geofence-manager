package com.hgyllensvard.geofencemanager.geofence;


import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

@Singleton
public class SelectedGeofenceId {

    private BehaviorSubject<Long> subject;

    public SelectedGeofenceId() {
        subject = BehaviorSubject.createDefault(Geofence.NO_ID);
    }

    public synchronized long selectedGeofenceId() {
        return subject.getValue();
    }

    /**
     * @return Observable returning the currently selected id.
     * This will always return a valid id, it does not guarantee that a
     * geofence exist with that id.
     * <p>
     * If you're only interested in the currently selected Geofence, see {@link SelectedGeofence}
     */
    public Observable<Long> observeValidSelectedGeofenceId() {
        return observeSelectedGeofenceId()
                .filter(geofenceId -> geofenceId != Geofence.NO_ID);
    }

    /**
     * @return Observable returning the id of the current {@link Geofence} selected.
     * If no Geofence is selected a no_id is set to notify of this change.
     */
    public Observable<Long> observeSelectedGeofenceId() {
        return subject
                .distinctUntilChanged();
    }

    public synchronized boolean isGeofenceSelected() {
        return subject.getValue() != Geofence.NO_ID;
    }

    public synchronized void setNoSelection() {
        Timber.d("No selected Geofence");
        subject.onNext(Geofence.NO_ID);
    }

    public synchronized boolean selectedGeofence(long geofenceId) {
        if (isInvalidGeofenceId(geofenceId)) {
            return false;
        }
        Timber.d("New Geofence selected: %s", geofenceId);
        subject.onNext(geofenceId);
        return true;
    }

    private boolean isInvalidGeofenceId(long geofenceId) {
        return geofenceId <= 0 && geofenceId != Geofence.NO_ID;
    }
}
