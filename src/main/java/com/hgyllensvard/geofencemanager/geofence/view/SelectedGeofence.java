package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

@AutoValue
public abstract class SelectedGeofence {

    public abstract boolean isGeofenceSelected();

    @Nullable
    public abstract GeofenceData selectedGeofence();

    public static SelectedGeofence noGeofenceSelected() {
        return new AutoValue_SelectedGeofence(false, null);
    }

    public static SelectedGeofence geofenceSelected(GeofenceData geofenceData) {
        return new AutoValue_SelectedGeofence(true, geofenceData);
    }
}
