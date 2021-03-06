package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import com.google.android.gms.location.GeofencingEvent;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class GeofenceTriggeredManager {

    private static final long INVALID_ID = -1L;

    private final PublishSubject<List<com.google.android.gms.location.Geofence>> enteredGeofencesSubject;
    private final PublishSubject<List<com.google.android.gms.location.Geofence>> dwellingGeofenceSubject;
    private final PublishSubject<List<com.google.android.gms.location.Geofence>> leftGeofenceSubject;
    private final Observable<Geofence> enteredGeofencesObservable;
    private final Observable<Geofence> dwellingGeofenceObservable;
    private final Observable<Geofence> leftGeofenceObservable;

    private final GeofenceManager geofenceManager;

    public GeofenceTriggeredManager(
            GeofenceManager geofenceManager
    ) {
        this.geofenceManager = geofenceManager;

        enteredGeofencesSubject = PublishSubject.create();
        dwellingGeofenceSubject = PublishSubject.create();
        leftGeofenceSubject = PublishSubject.create();

        enteredGeofencesObservable = convertToGeofenceObservable(enteredGeofencesSubject);
        dwellingGeofenceObservable = convertToGeofenceObservable(dwellingGeofenceSubject);
        leftGeofenceObservable = convertToGeofenceObservable(leftGeofenceSubject);
    }

    public Observable<Geofence> observeEnteredGeofences() {
        return enteredGeofencesObservable;
    }

    public Observable<Geofence> observeGeofencesDwelling() {
        return dwellingGeofenceObservable;
    }

    public Observable<Geofence> observeLeftGeofence() {
        return leftGeofenceObservable;
    }

    void geofencesTriggered(GeofencingEvent geofenceEvent) {
        List<com.google.android.gms.location.Geofence> geofences = geofenceEvent.getTriggeringGeofences();

        switch (geofenceEvent.getGeofenceTransition()) {
            case com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER:
                enteredGeofencesSubject.onNext(geofences);
                break;
            case com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT:
                leftGeofenceSubject.onNext(geofences);
                break;
            case com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_DWELL:
                dwellingGeofenceSubject.onNext(geofences);
                break;
            default:
                Timber.w("Unknown geofence transition: %s", geofenceEvent.getGeofenceTransition());
        }
    }

    private Observable<Geofence> convertToGeofenceObservable(PublishSubject<List<com.google.android.gms.location.Geofence>> subject) {
        return subject.flatMap(Observable::fromIterable)
                .map(this::convertToGeofenceIdentifier)
                .filter(identifier -> identifier != INVALID_ID)
                .flatMap(geofenceId -> geofenceManager.getGeofence(geofenceId)
                        .toObservable())
                .filter(GeofenceResult::success)
                .map(GeofenceResult::geofence)
                .subscribeOn(Schedulers.io());
    }

    private long convertToGeofenceIdentifier(com.google.android.gms.location.Geofence geofence) {
        try {
            return Long.valueOf(geofence.getRequestId());
        } catch (NumberFormatException e) {
            Timber.e(e, "Geofence: %s contained an invalid id", geofence);
            return INVALID_ID;
        }
    }
}