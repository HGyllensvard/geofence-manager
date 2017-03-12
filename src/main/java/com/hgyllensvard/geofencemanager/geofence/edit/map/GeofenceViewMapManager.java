package com.hgyllensvard.geofencemanager.geofence.edit.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

public class GeofenceViewMapManager {

    private final GeofenceView geofenceView;

    private Marker marker;
    private Circle circle;

    public GeofenceViewMapManager(
            GeofenceView geofenceView
    ) {
        this.geofenceView = geofenceView;
    }

    void display(GoogleMap map) {
        marker = map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_select_geofence))
                .position(geofenceView.latLng())
                .draggable(false));

        circle = map.addCircle(new CircleOptions()
                .fillColor(geofenceView.fillColor())
                .strokeColor(geofenceView.strokeColor())
                .center(geofenceView.latLng())
                .radius(geofenceView.radius()));
    }

    void remove() {
        if (marker != null) {
            marker.remove();
            marker = null;
        }

        if (circle != null) {
            circle.remove();
            circle = null;
        }
    }

    boolean isMarker(String markerId) {
        return marker.getId().equals(markerId);
    }

    Geofence getGeofence() {
        return geofenceView.geofence();
    }
}
