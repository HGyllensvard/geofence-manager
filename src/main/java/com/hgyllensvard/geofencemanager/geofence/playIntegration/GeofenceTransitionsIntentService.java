package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.GeofencingEvent;
import com.hgyllensvard.geofencemanager.di.GeofenceModuleManager;

import javax.inject.Inject;

import timber.log.Timber;

public class GeofenceTransitionsIntentService extends IntentService {

    @Inject
    GeofenceTriggeredManager geofenceEventTriggerManager;

    public GeofenceTransitionsIntentService() {
        super(GeofenceTransitionsIntentService.class.getSimpleName());

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

        geofenceEventTriggerManager.geofencesTriggered(geofenceEvent);
    }
}