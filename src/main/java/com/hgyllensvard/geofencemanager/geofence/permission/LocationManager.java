package com.hgyllensvard.geofencemanager.geofence.permission;

import android.location.Criteria;
import android.location.Location;
import android.support.annotation.Nullable;

import io.reactivex.Single;

public class LocationManager {

    private android.location.LocationManager locationManager;
    private LocationPermissionRequester locationPermissionRequester;

    public LocationManager(
            android.location.LocationManager locationManager,
            LocationPermissionRequester locationPermissionRequester
    ) {

        this.locationManager = locationManager;
        this.locationPermissionRequester = locationPermissionRequester;
    }

    public Single<RequestPermissionResult> request() {
        return locationPermissionRequester.request();
    }

    @Nullable
    public Location getLocation() throws SecurityException {
        return locationManager.getLastKnownLocation(
                locationManager.getBestProvider(new Criteria(), false));
    }
}
