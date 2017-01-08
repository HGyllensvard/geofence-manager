package com.hgyllensvard.geofencemanager.geofence.permission;

import android.location.Criteria;
import android.location.Location;

import io.reactivex.Single;

public class LocationManager {

    android.location.LocationManager locationManager;
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

    public Location getLocation() throws SecurityException {
        Criteria criteria = new Criteria();
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }
}
