package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

class PlayApiManager {

    private final GoogleApiClient googleApiClient;

    PlayApiManager(
            Context context
    ) {
        googleApiClient = new Builder(context)
                .addApi(LocationServices.API)
                .build();
    }

    Single<GoogleApiClient> connectToPlayServices() {
        if (googleApiClient.isConnected()) {
            return Single.just(googleApiClient);
        }

        return Single.create((SingleOnSubscribe<GoogleApiClient>) emitter -> {
            googleApiClient.registerConnectionCallbacks(new ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    if (emitter.isDisposed()) {
                        return;
                    }

                    emitter.onSuccess(googleApiClient);
                }

                @Override
                public void onConnectionSuspended(int i) {
                    if (emitter.isDisposed()) {
                        return;
                    }

                    emitter.onError(new IllegalStateException("Connection to play services suspended"));
                }
            });

            googleApiClient.registerConnectionFailedListener(result -> {
                if (result.hasResolution()) {
//                TODO Manage to try and resolve situation
                } else {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    emitter.onError(new IllegalStateException(result.getErrorMessage()));
                }
            });

            googleApiClient.connect();
        }).subscribeOn(Schedulers.io());
    }
}
