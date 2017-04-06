package com.hgyllensvard.geofencemanager.geofence.edit.map.geofenceView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.edit.map.util.MapCalculationsUtils;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import timber.log.Timber;

public class GeofenceMapView {

    private final GeofenceView geofenceView;
    private final GeofenceMapOptions geofenceMapOptions;
    private final MapCalculationsUtils mapCalculationsUtils;

    private Marker centreMarker;

    private Circle circle;
    private Marker resizeMarker;

    GeofenceMapView(
            GeofenceView geofenceView,
            GeofenceMapOptions geofenceMapOptions,
            MapCalculationsUtils mapCalculationsUtils
    ) {
        this.geofenceView = geofenceView;
        this.geofenceMapOptions = geofenceMapOptions;
        this.mapCalculationsUtils = mapCalculationsUtils;
    }

    public void display(GoogleMap map) {
        circle = map.addCircle(new CircleOptions()
                .fillColor(geofenceView.fillColor())
                .strokeColor(geofenceView.strokeColor())
                .center(geofenceView.latLng())
                .radius(geofenceView.radius()));

        centreMarker = map.addMarker(new MarkerOptions()
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(geofenceMapOptions.centreMarkerDrawable()))
                .position(geofenceView.latLng()));

        resizeMarker = map.addMarker(new MarkerOptions()
                .position(calculateResizeRadius())
                .icon(BitmapDescriptorFactory.fromResource(geofenceMapOptions.resizeMarkerDrawable()))
                .draggable(true));
    }

    public void markerUpdate(Marker marker) {
        String markerId = marker.getId();

        if (isCentreMarker(markerId)) {
            LatLng latLng = marker.getPosition();

            Timber.v("Updating centre position: %s", latLng);
            circle.setCenter(latLng);
            resizeMarker.setPosition(calculateResizeRadius());
        } else if (isResizeMarker(markerId)) {
            float newRadius = mapCalculationsUtils.toRadiusMeters(
                    centreMarker.getPosition(),
                    marker.getPosition());

            Timber.v("Updating resizing: %s", newRadius);

            if (isRadiusTooSmall(newRadius) || isRadiusTooBig(newRadius)) {
                resizeMarker.setPosition(calculateResizeRadius());
            } else {
                circle.setRadius(newRadius);
            }
        } else {
            throw new IllegalArgumentException("Tried to update with a marker not belonging to this view: " + marker);
        }
    }

    public void remove() {
        if (centreMarker != null) {
            centreMarker.remove();
            centreMarker = null;
        }

        if (circle != null) {
            circle.remove();
            circle = null;
        }

        if (resizeMarker != null) {
            resizeMarker.remove();
            resizeMarker = null;
        }
    }

    public boolean isMarker(String markerId) {
        return isCentreMarker(markerId) || isResizeMarker(markerId);
    }

    public Geofence getGeofence() {
        return geofenceView
                .geofence()
                .withLatLng(centreMarker.getPosition())
                .withRadius((float) circle.getRadius());
    }

    public GeofenceView getGeofenceView() {
        return geofenceView;
    }

    private boolean isCentreMarker(String markerId) {
        return centreMarker.getId().equals(markerId);
    }

    private boolean isResizeMarker(String markerId) {
        return resizeMarker.getId().equals(markerId);
    }

    private boolean isRadiusTooBig(float newRadius) {
        return newRadius > geofenceMapOptions.maximumGeofenceSize();
    }

    private boolean isRadiusTooSmall(float newRadius) {
        return newRadius < geofenceMapOptions.minimumGeofenceSize();
    }

    private LatLng calculateResizeRadius() {
        return mapCalculationsUtils.toRadiusLatLng(centreMarker.getPosition(), circle.getRadius());
    }
}
