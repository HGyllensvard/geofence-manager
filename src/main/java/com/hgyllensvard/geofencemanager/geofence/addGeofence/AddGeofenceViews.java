package com.hgyllensvard.geofencemanager.geofence.addGeofence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface AddGeofenceViews extends View {

    Flowable<Boolean> displayMap();

    Flowable<LatLng> observerLongClick();
}
