package com.hgyllensvard.geofencemanager.geofence.geofence;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AddGeofenceResult {

    public abstract boolean success();

    @Nullable
    public abstract Geofence geofence();

    @Nullable
    public abstract Throwable error();

    public static AddGeofenceResult success(Geofence geofence) {
        return new AutoValue_AddGeofenceResult(true, geofence, null);
    }

    public static AddGeofenceResult failure(Throwable throwable) {
        return new AutoValue_AddGeofenceResult(false, null, throwable);
    }
}
