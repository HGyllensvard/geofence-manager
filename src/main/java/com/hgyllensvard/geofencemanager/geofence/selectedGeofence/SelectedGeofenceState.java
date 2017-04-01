package com.hgyllensvard.geofencemanager.geofence.selectedGeofence;

import com.google.auto.value.AutoValue;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

@AutoValue
public abstract class SelectedGeofenceState {

    public static final SelectedGeofenceState NO_GEOFENCE_SELECTED = noSelection();

    public abstract boolean validGeofence();

    public abstract Geofence geofence();

    private static SelectedGeofenceState noSelection() {
        return new AutoValue_SelectedGeofenceState(false, Geofence.sDummyGeofence);
    }

    public static SelectedGeofenceState selectedGeofence(Geofence geofence) {
        return new AutoValue_SelectedGeofenceState(true, geofence);
    }
}
