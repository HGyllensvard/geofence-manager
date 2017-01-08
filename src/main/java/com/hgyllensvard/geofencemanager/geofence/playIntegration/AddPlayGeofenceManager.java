package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingRequest;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

class AddPlayGeofenceManager {

    private final Context context;
    private GeofencingApi geofencingApi;

    AddPlayGeofenceManager(
            Context context,
            GeofencingApi geofencingApi
    ) {
        this.context = context;
        this.geofencingApi = geofencingApi;
    }

    Single<Boolean> addGeofence(
            Geofence geofence,
            GoogleApiClient googleApiClient
    ) throws SecurityException {
        return Single.create((SingleOnSubscribe<Boolean>) emitter ->
                geofencingApi.addGeofences(
                        googleApiClient,
                        getGeofencingRequest(geofence),
                        getGeofencePendingIntent()
                ).setResultCallback(status -> {
                    if (status.isSuccess()) {
                        emitter.onSuccess(true);
                    } else {
                        emitter.onError(new IllegalStateException("Failed to save"));
                    }
                })).subscribeOn(Schedulers.io());
    }

    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofence(geofence);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
