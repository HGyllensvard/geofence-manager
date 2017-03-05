package com.hgyllensvard.geofencemanager.geofence;


import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public class SelectedGeofence {

    private BehaviorSubject<Long> subject;

    public SelectedGeofence() {
        subject = BehaviorSubject.createDefault(Geofence.NO_ID);
    }

    public synchronized long selectedGeofence() {
        return subject.getValue();
    }

    public Observable<Long> observeSelectedGeofence() {
        return subject;
    }

    public synchronized void noSelection() {
        Timber.d("No selected Geofence");
        subject.onNext(Geofence.NO_ID);
    }

    public synchronized void updatedSelectedGeofence(long geofenceId) {
        Timber.d("New Geofence selected: %s", geofenceId);
        subject.onNext(geofenceId);
    }
}
