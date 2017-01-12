package com.hgyllensvard.geofencemanager.geofence.addGeofence;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import io.reactivex.Flowable;

public class AddGeofenceView implements AddGeofenceViews {

    private final MapView mapView;

    public AddGeofenceView(
            MapView mapView
    ) {
        this.mapView = mapView;
    }

    @Override
    public Flowable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public Flowable<LatLng> observerLongClick() {
        return mapView.observerLongClick();
    }

    @Override
    public void destroy() {

    }
}
