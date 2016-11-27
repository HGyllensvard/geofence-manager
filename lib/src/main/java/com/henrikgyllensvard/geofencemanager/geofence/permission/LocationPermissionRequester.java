package com.henrikgyllensvard.geofencemanager.geofence.permission;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Single;

public class LocationPermissionRequester {

    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    
    private Activity activity;

    public LocationPermissionRequester(Activity activity) {
        this.activity = activity;
    }

    public Single<RequestPermissionResult> request() {
        return Single.defer(() -> {
            int permissionCheck = ContextCompat.checkSelfPermission(activity,
                    ACCESS_FINE_LOCATION);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                return Single.just(RequestPermissionResult.GRANTED);
            } else {
                return informUserWhyPermissionNeeded()
                        .flatMap(aVoid -> requestPermission());
            }
        });
    }

    private Single<Boolean> informUserWhyPermissionNeeded() {
        return Single.just(true);
    }

    private Single<RequestPermissionResult> requestPermission() {
        return RxPermissions.getInstance(activity)
                .request(ACCESS_FINE_LOCATION)
                .singleOrError()
                .map(granted -> granted ? RequestPermissionResult.GRANTED : RequestPermissionResult.DENIED);
    }
}
