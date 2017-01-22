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
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

class AddPlayGeofenceManager {

    private final Context context;
    private final GeofencingApi geofencingApi;
    private final PlayApiManager playApiManager;

    AddPlayGeofenceManager(
            Context context,
            GeofencingApi geofencingApi,
            PlayApiManager playApiManager
    ) {
        this.context = context;
        this.geofencingApi = geofencingApi;
        this.playApiManager = playApiManager;
    }

    Single<Boolean> addGeofence(
            Geofence geofence
    ) throws SecurityException {
        return playApiManager.connectToPlayServices()
                .flatMap(googleApiClient -> Single.create((SingleOnSubscribe<Boolean>) emitter -> {
                    if (emitter.isDisposed()) {
                        return;
                    }

                    geofencingApi.addGeofences(
                            googleApiClient,
                            getGeofencingRequest(geofence),
                            getGeofencePendingIntent()
                    ).setResultCallback(status -> {
                        if (emitter.isDisposed()) {
                            return;
                        }

                        emitter.onSuccess(status.isSuccess());
                    });
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
