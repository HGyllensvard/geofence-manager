package com.hgyllensvard.geofencemanager.geofence.view;

import com.google.android.gms.maps.CameraUpdate;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;

import io.reactivex.Single;

public interface GeofenceManagerView extends View {

    Single<Boolean> displayMap();

    void animateCameraTo(CameraUpdate cameraUpdate);
}
