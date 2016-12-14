package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

class RemovePlayGeofenceManager {

    RemovePlayGeofenceManager() {
    }

    // TODO Manage the status callback in a more gracious way?
    Single<Boolean> removeGeofence(GoogleApiClient googleApiClient, String name) {
        return Single.fromCallable(() -> LocationServices.GeofencingApi.removeGeofences(googleApiClient, Collections.singletonList(name)))
                .map(statusPendingResult -> statusPendingResult.await().isSuccess())
                .subscribeOn(Schedulers.io());
    }
}
