package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

public class GeofenceMarker {

    private final GeofenceData geofenceData;
    private final int fillColor;
    private final int strokeColor;

    private Marker marker;
    private Circle circle;

    public GeofenceMarker(
            GeofenceData geofenceData,
            @ColorInt int fillColor,
            @ColorInt int strokeColor
    ) {
        this.geofenceData = geofenceData;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
    }

    public void display(GoogleMap map) {
        marker = map.addMarker(new MarkerOptions()
                .position(geofenceData.latLng())
                .draggable(true));

        circle = map.addCircle(new CircleOptions()
                .fillColor(fillColor)
                .strokeColor(strokeColor)
                .center(geofenceData.latLng())
                .radius(geofenceData.radius()));
    }

    public void remove() {
        marker.remove();
        circle.remove();
    }

    public boolean isMarker(String markerId) {
        return marker.getId().equals(markerId);
    }
}
