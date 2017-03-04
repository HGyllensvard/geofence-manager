package com.hgyllensvard.geofencemanager.geofence;


import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

public class SelectedGeofence {

    private BehaviorSubject<Long> subject;

    public SelectedGeofence() {
        subject = BehaviorSubject.create();
    }

    public Observable<Long> observeSelectedGeofence() {
        return subject;
    }

    public synchronized void updatedSelectedGeofence(long geofenceId) {
        Timber.d("New Geofence selected: %s", geofenceId);
        subject.onNext(geofenceId);
    }
}
