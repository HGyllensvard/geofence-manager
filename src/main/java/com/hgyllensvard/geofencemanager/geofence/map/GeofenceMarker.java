package com.hgyllensvard.geofencemanager.geofence.map;

import android.support.annotation.ColorInt;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgyllensvard.geofencemanager.geofence.Geofence;

public class GeofenceMarker {

    private final Geofence geofence;
    private final int fillColor;
    private final int strokeColor;

    private Marker marker;
    private Circle circle;

    public GeofenceMarker(
            Geofence geofence,
            @ColorInt int fillColor,
            @ColorInt int strokeColor
    ) {
        this.geofence = geofence;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
    }

    public void display(GoogleMap map) {
        marker = map.addMarker(new MarkerOptions()
                .position(geofence.latLng())
                .draggable(true));

        circle = map.addCircle(new CircleOptions()
                .fillColor(fillColor)
                .strokeColor(strokeColor)
                .center(geofence.latLng())
                .radius(geofence.radius()));
    }

    public void remove() {
        if (marker != null) {
            marker.remove();
            marker = null;
        }

        if (circle != null) {
            circle.remove();
            circle = null;
        }
    }

    public Geofence getGeofence() {
        return geofence;
    }

    public boolean isMarker(String markerId) {
        return marker.getId().equals(markerId);
    }

    @Override
    public String toString() {
        return "GeofenceMarker{" +
                "geofence=" + geofence +
                ", marker=" + marker +
                ", circle=" + circle +
                '}';
    }
}
