package com.hgyllensvard.geofencemanager.geofence;

import com.google.android.gms.maps.model.LatLng;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeofenceData {

    public abstract String name();

    public abstract LatLng latLng();

    public abstract int radius();

    public static GeofenceData create(String name, long latitude, long longitude, int radius) {
        return new AutoValue_GeofenceData(name, new LatLng(latitude, longitude), radius);
    }

    public static GeofenceData create(String name, LatLng latLng, int radius) {
        return new AutoValue_GeofenceData(name, latLng, radius);
    }
}
