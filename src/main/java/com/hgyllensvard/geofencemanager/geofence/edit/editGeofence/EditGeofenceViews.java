package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;

import io.reactivex.Observable;

public interface EditGeofenceViews extends View {

    Observable<Boolean> displayMap();

    Observable<Long> observeGeofenceSelected();

    Observable<Integer> observeCameraStartedMoving();

    void instantlyHideSelectedGeofenceOptions();

    void hideSelectedGeofenceOptions();

    void displaySelectedGeofenceOptions();

    Observable<Boolean> observeDeleteGeofence();

    void exitView();

    String getGeofenceName();

    @Nullable
    LatLng getGeofencePosition(long geofenceId);

    void displayGeofenceName(String name);
}
