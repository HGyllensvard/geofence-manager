package com.hgyllensvard.geofencemanager.geofence.geofence;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeofenceResult {

    public abstract boolean success();

    public abstract Geofence geofence();

    public static GeofenceResult fail() {
        return new AutoValue_GeofenceResult(false, Geofence.sDummyGeofence);
    }

    public static GeofenceResult success(Geofence geofence) {
        return new AutoValue_GeofenceResult(true, geofence);
    }
}
