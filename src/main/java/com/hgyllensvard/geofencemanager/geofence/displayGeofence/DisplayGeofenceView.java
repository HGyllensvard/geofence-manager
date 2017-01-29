package com.hgyllensvard.geofencemanager.geofence.displayGeofence;


import com.google.android.gms.maps.CameraUpdate;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceView;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import java.util.List;

import io.reactivex.Observable;

public class DisplayGeofenceView implements DisplayGeofenceViews {

    private final MapView mapView;

    public DisplayGeofenceView(
            MapView mapView
    ) {
        this.mapView = mapView;
    }

    @Override
    public Observable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public void displayGeofenceViews(List<GeofenceView> geofenceViews) {
        mapView.updateGeofenceViews(geofenceViews);
    }

    @Override
    public void removeGeofenceViews(List<GeofenceView> geofenceViews) {
        mapView.removedGeofenceViews(geofenceViews);
    }

    @Override
    public void animateCameraTo(CameraUpdate cameraUpdate) {
        mapView.animateCameraTo(cameraUpdate);
    }

    @Override
    public void destroy() {

    }
}
