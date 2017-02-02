package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.hgyllensvard.geofencemanager.di.GeofenceModuleManager;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class GeofenceTransitionsIntentService extends IntentService {

    @Inject
    private GeofenceEventTriggeredManager geofenceEventTriggeredManager;

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        GeofenceModuleManager.geofenceComponent(getApplicationContext())
                .inject(this);
    }

    /**
     * Handles incoming intents.
     *
     * @param intent The Intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofenceEvent = GeofencingEvent.fromIntent(intent);
        if (geofenceEvent.hasError()) {
            int errorCode = geofenceEvent.getErrorCode();
            Timber.e("Location Services error: " + errorCode);
            return;
        }

        List<Geofence> triggeredGeofences = geofenceEvent.getTriggeringGeofences();
        switch (geofenceEvent.getGeofenceTransition()) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                manageEnteringGeofence(triggeredGeofences);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                manageExitingGeofence(triggeredGeofences);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                manageDwellingGeofence(triggeredGeofences);
                break;
            default:
                Timber.w("Unknown geofence transition: %s", geofenceEvent.getGeofenceTransition());
        }
    }

    private void manageEnteringGeofence(List<Geofence> triggeredGeofences) {
        geofenceEventTriggeredManager.triggerEntered(triggeredGeofences);
    }

    private void manageExitingGeofence(List<Geofence> triggeredGeofences) {
        geofenceEventTriggeredManager.triggerLeft(triggeredGeofences);
    }

    private void manageDwellingGeofence(List<Geofence> triggeredGeofences) {
        geofenceEventTriggeredManager.triggerDwelling(triggeredGeofences);
    }
}