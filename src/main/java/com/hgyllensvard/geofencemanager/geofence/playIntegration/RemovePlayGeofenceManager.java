package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.location.GeofencingApi;

import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

class RemovePlayGeofenceManager {

    private final GeofencingApi geofencingApi;
    private final PlayApiManager playApiManager;

    RemovePlayGeofenceManager(
            GeofencingApi geofencingApi,
            PlayApiManager playApiManager
    ) {
        this.geofencingApi = geofencingApi;
        this.playApiManager = playApiManager;
    }

    // TODO Manage the status callback (errors etc) in a more gracious way?
    Single<Boolean> removeGeofence(String name) {
        return playApiManager.connectToPlayServices()
                .flatMap(googleApiClient ->
                        Single.create((SingleOnSubscribe<Boolean>) emitter -> {
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
                        })).subscribeOn(Schedulers.io());
    }
}
