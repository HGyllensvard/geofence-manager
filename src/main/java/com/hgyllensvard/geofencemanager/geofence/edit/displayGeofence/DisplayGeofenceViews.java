package com.hgyllensvard.geofencemanager.geofence.edit.displayGeofence;


import com.google.android.gms.maps.CameraUpdate;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;
import com.hgyllensvard.geofencemanager.geofence.edit.map.geofenceView.GeofenceView;

import java.util.List;

import io.reactivex.Observable;

public interface DisplayGeofenceViews extends View {

    Observable<Boolean> displayMap();

    void displayGeofenceViews(List<GeofenceView> geofenceViews);

    void removeGeofenceViews(List<GeofenceView> geofenceViews);

    void moveCameraTo(CameraUpdate cameraUpdate);
}
