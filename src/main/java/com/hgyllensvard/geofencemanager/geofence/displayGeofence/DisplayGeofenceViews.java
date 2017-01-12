package com.hgyllensvard.geofencemanager.geofence.displayGeofence;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.Marker;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMarker;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DisplayGeofenceViews extends View {

    Flowable<Boolean> displayMap();

    void displayGeofences(List<GeofenceMarker> markers);

    void animateCameraTo(CameraUpdate cameraUpdate);
}
