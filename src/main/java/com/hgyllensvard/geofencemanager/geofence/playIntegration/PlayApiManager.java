package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PlayApiManager {

    private final GoogleApiClient googleApiClient;

    public PlayApiManager(
            Context context
    ) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
    }

    public Single<GoogleApiClient> connectToPlayServices() {
        if (googleApiClient.isConnected()) {
            return Single.just(googleApiClient);
        }

        return Single.fromCallable(() -> {
            ConnectionResult result = googleApiClient.blockingConnect(3, TimeUnit.SECONDS);

            if (result.isSuccess()) {
                return googleApiClient;
            }

            if (result.hasResolution()) {
//                TODO Manage to try and resolve situation
            } else {
                Timber.e("Failed to connect to google play without any resolution, error code: %s and error message: %s",
                        result.getErrorCode(),
                        result.getErrorMessage());
            }

            throw new IllegalStateException(result.getErrorMessage());
        }).subscribeOn(Schedulers.io());
    }
}
