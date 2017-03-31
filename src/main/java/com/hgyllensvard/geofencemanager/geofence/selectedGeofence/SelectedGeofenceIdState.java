package com.hgyllensvard.geofencemanager.geofence.selectedGeofence;


import com.google.auto.value.AutoValue;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

@AutoValue
public abstract class SelectedGeofenceIdState {

    public abstract boolean isGeofenceSelected();

    public abstract long geofenceId();

    public static SelectedGeofenceIdState noSelection() {
        return new AutoValue_SelectedGeofenceIdState(false, Geofence.NO_ID);
    }

    public static SelectedGeofenceIdState selected(long geofenceId) {
        return new AutoValue_SelectedGeofenceIdState(true, geofenceId);
    }
}
