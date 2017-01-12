package com.hgyllensvard.geofencemanager.geofence;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class SelectedGeofence {

    public abstract boolean isGeofenceSelected();

    @Nullable
    public abstract Geofence selectedGeofence();

    public static SelectedGeofence noGeofenceSelected() {
        return new AutoValue_SelectedGeofence(false, null);
    }

    public static SelectedGeofence geofenceSelected(Geofence geofence) {
        return new AutoValue_SelectedGeofence(true, geofence);
    }
}
