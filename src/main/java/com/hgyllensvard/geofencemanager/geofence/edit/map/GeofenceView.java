package com.hgyllensvard.geofencemanager.geofence.edit.map;

import android.support.annotation.ColorInt;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

public class GeofenceView {

    private final Geofence geofence;
    private final int fillColor;
    private final int strokeColor;

    private Marker marker;
    private Circle circle;

    public GeofenceView(
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
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_select_geofence))
                .position(geofence.latLng())
                .draggable(false));

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
        return "GeofenceView{" +
                "geofence=" + geofence +
                ", marker=" + marker +
                ", circle=" + circle +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeofenceView marker = (GeofenceView) o;

        if (fillColor != marker.fillColor) return false;
        if (strokeColor != marker.strokeColor) return false;
        return geofence != null ? geofence.equals(marker.geofence) : marker.geofence == null;

    }

    @Override
    public int hashCode() {
        int result = geofence != null ? geofence.hashCode() : 0;
        result = 31 * result + fillColor;
        result = 31 * result + strokeColor;
        return result;
    }
}
