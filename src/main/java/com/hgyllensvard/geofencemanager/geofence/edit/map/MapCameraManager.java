package com.hgyllensvard.geofencemanager.geofence.edit.map;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;

import javax.inject.Inject;

import timber.log.Timber;

public class MapCameraManager {

    public static final int DEFAULT_ZOOM_LEVEL = 16;

    public LocationManager locationManager;

    @Inject
    public MapCameraManager(
            LocationManager locationManager
    ) {
        this.locationManager = locationManager;
    }

    /**
     * @return The closet known location of the user, if no such location is known the cameraUpdate will zoom out.
     * @throws SecurityException If the application is not allowed location permissions
     */
    @NonNull
    public CameraUpdate userPosition() throws SecurityException {
        Location location = locationManager.getLocation();

        if (location == null) {
            Timber.w("Could not fetch the latest position");
            return CameraUpdateFactory.zoomOut();
        }

        return CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(DEFAULT_ZOOM_LEVEL)
                .build());
    }
}
