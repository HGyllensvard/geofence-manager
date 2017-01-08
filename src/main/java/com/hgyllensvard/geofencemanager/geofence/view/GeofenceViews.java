package com.hgyllensvard.geofencemanager.geofence.view;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface GeofenceViews extends View {

    Single<Boolean> displayMap();

    void animateCameraTo(CameraUpdate cameraUpdate);

    Flowable<LatLng> observerLongClick();

    void displayGeofences(List<GeofenceData> geofenceDatas);

    Flowable<SelectedGeofence> observeGeofenceSelected();

    void displaySelectedGeofenceOptions();

    void hideSelectedGeofenceOptions();
}
