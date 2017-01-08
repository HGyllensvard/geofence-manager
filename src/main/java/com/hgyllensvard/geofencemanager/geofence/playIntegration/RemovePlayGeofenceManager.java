package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.GeofencingApi;

import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

class RemovePlayGeofenceManager {

    private GeofencingApi geofencingApi;

    RemovePlayGeofenceManager(GeofencingApi geofencingApi) {
        this.geofencingApi = geofencingApi;
    }

    // TODO Manage the status callback in a more gracious way?
    Single<Boolean> removeGeofence(GoogleApiClient googleApiClient, String name) {
        return Single.create((SingleOnSubscribe<Boolean>) emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            geofencingApi.removeGeofences(googleApiClient, Collections.singletonList(name))
                    .setResultCallback(status -> {
                        if (emitter.isDisposed()) {
                            return;
                        }

                        if (status.isSuccess()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onError(new IllegalStateException("Failed to save"));
                        }
                    });
        }).subscribeOn(Schedulers.io());
    }
}
