package com.hgyllensvard.geofencemanager.geofence.displayGeofence;


import com.google.android.gms.maps.CameraUpdate;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;
import com.hgyllensvard.geofencemanager.geofence.Geofence;

import java.util.List;

import io.reactivex.Observable;

public interface DisplayGeofenceViews extends View {

    Observable<Boolean> displayMap();

    void displayGeofences(List<Geofence> markers);

    void animateCameraTo(CameraUpdate cameraUpdate);
}
