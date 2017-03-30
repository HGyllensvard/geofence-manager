package com.hgyllensvard.geofencemanager.geofence.edit.map.dragging;

import com.google.android.gms.maps.model.LatLng;
import com.google.auto.value.AutoValue;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

@AutoValue
public abstract class DragEvent {

    public abstract DragAction action();

    public abstract Geofence geofence();

    public abstract LatLng newPosition();

    public static DragEvent dragStarted(Geofence geofence, LatLng latLng) {
        return new AutoValue_DragEvent(DragAction.DRAG_STARTED, geofence, latLng);
    }

    public static DragEvent dragging(Geofence geofence, LatLng latLng) {
        return new AutoValue_DragEvent(DragAction.DRAGGING, geofence, latLng);
    }

    public static DragEvent draggingEnded(Geofence geofence, LatLng latLng) {
        return new AutoValue_DragEvent(DragAction.DRAG_ENDED, geofence, latLng);
    }
}
