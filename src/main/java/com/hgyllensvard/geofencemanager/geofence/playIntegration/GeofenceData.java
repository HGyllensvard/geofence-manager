package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.maps.model.LatLng;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeofenceData {

    public abstract String name();

    public abstract LatLng latLng();

    public static GeofenceData create(String name, long latitude, long longitude) {
        return new AutoValue_GeofenceData(name, new LatLng(latitude, longitude));
    }

    public static GeofenceData create(String name, LatLng latLng) {
        return new AutoValue_GeofenceData(name, latLng);
    }
}
