package com.hgyllensvard.geofencemanager.geofence.edit.map.dragging;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.auto.value.AutoValue;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

@AutoValue
public abstract class DragEvent {

    public abstract DragAction action();

    public abstract Geofence geofence();

    public abstract Marker marker();

    public static DragEvent dragStarted(Geofence geofence, Marker marker) {
        return new AutoValue_DragEvent(DragAction.DRAG_STARTED, geofence, marker);
    }

    public static DragEvent dragging(Geofence geofence, Marker marker) {
        return new AutoValue_DragEvent(DragAction.DRAGGING, geofence, marker);
    }

    public static DragEvent draggingEnded(Geofence geofence, Marker marker) {
        return new AutoValue_DragEvent(DragAction.DRAG_ENDED, geofence, marker);
    }
}
