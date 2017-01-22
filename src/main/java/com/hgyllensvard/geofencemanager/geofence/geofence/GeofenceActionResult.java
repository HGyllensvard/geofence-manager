package com.hgyllensvard.geofencemanager.geofence.geofence;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeofenceActionResult {

    public abstract boolean success();

    @Nullable
    public abstract Geofence geofence();

    @Nullable
    public abstract Throwable error();

    public static GeofenceActionResult success(Geofence geofence) {
        return new AutoValue_GeofenceActionResult(true, geofence, null);
    }

    public static GeofenceActionResult failure(Throwable throwable) {
        return new AutoValue_GeofenceActionResult(false, null, throwable);
    }
}
