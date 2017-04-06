package com.hgyllensvard.geofencemanager.geofence.edit.map.util;


import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;

@PerActivity
public class MapCalculationsUtils {

    private static final double RADIUS_OF_EARTH_METERS = 6371009;

    public MapCalculationsUtils() {

    }

    public LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) / Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    public int toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude, radius.latitude, radius.longitude, result);
        return Math.round(result[0]);
    }
}
