package com.hgyllensvard.geofencemanager.geofence.view;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

public class GeofenceMarker {

    private final GeofenceData geofenceData;

    private Marker marker;
    private Circle circle;

    public GeofenceMarker(GeofenceData geofenceData) {
        this.geofenceData = geofenceData;
    }

    public void display(GoogleMap map) {
        marker = map.addMarker(new MarkerOptions()
                .position(geofenceData.latLng())
                .draggable(true));

        circle = map.addCircle(new CircleOptions()
                .center(geofenceData.latLng())
                .radius(geofenceData.radius()));
    }

    public void remove() {
        marker.remove();
        circle.remove();
    }
}
