package com.hgyllensvard.geofencemanager.geofence.addGeofence;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class AddGeofenceView implements AddGeofenceViews {

    private final MapView mapView;

    public AddGeofenceView(
            MapView mapView
    ) {
        this.mapView = mapView;
    }

    @Override
    public Observable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public Observable<LatLng> observerLongClick() {
        return mapView.observerLongClick();
    }

    @Override
    public void destroy() {

    }
}
