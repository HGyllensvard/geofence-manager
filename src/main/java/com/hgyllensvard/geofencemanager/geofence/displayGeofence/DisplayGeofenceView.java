package com.hgyllensvard.geofencemanager.geofence.displayGeofence;


import com.google.android.gms.maps.CameraUpdate;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMarker;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import java.util.List;

import io.reactivex.Flowable;

public class DisplayGeofenceView implements DisplayGeofenceViews {

    private final MapView mapView;

    public DisplayGeofenceView(
            MapView mapView
    ) {
        this.mapView = mapView;
    }

    @Override
    public Flowable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public void displayGeofences(List<GeofenceMarker> markers) {
        mapView.displayMarkers(markers);
    }

    @Override
    public void animateCameraTo(CameraUpdate cameraUpdate) {
        mapView.animateCameraTo(cameraUpdate);
    }

    @Override
    public void destroy() {

    }
}
