package com.hgyllensvard.geofencemanager.geofence.edit.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import timber.log.Timber;

class GeofenceViewMapManager {

    private final GeofenceView geofenceView;

    private Marker marker;
    private Circle circle;

    GeofenceViewMapManager(
            GeofenceView geofenceView
    ) {
        this.geofenceView = geofenceView;
    }

    void display(GoogleMap map) {
        circle = map.addCircle(new CircleOptions()
                .fillColor(geofenceView.fillColor())
                .strokeColor(geofenceView.strokeColor())
                .center(geofenceView.latLng())
                .radius(geofenceView.radius()));

        marker = map.addMarker(new MarkerOptions()
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_select_geofence))
                .position(geofenceView.latLng()));
    }

    void updatePosition(LatLng latLng) {
        Timber.v("Updating position: %s", latLng);
        marker.setPosition(latLng);
        circle.setCenter(latLng);
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

    LatLng position() {
        return marker.getPosition();
    }

    boolean isMarker(String markerId) {
        return marker.getId().equals(markerId);
    }

    Geofence getGeofence() {
        return geofenceView.geofence();
    }

    GeofenceView getGeofenceView() {
        return geofenceView;
    }
}
