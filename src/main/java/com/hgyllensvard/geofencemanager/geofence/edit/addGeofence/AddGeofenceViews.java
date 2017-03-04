package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;

import io.reactivex.Observable;

public interface AddGeofenceViews extends View {

    Observable<Boolean> displayMap();

    Observable<LatLng> observerLongClick();
}
