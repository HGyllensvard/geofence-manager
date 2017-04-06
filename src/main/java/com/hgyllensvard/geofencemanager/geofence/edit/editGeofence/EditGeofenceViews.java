package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.support.annotation.Nullable;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import io.reactivex.Observable;

public interface EditGeofenceViews extends View {

    Observable<Boolean> displayMap();

    Observable<Long> observeGeofenceSelected();

    Observable<Integer> observeCameraStartedMoving();

    void hideSelectedGeofenceOptions();

    void displaySelectedGeofenceOptions();

    Observable<Boolean> observeDeleteGeofence();

    void exitView();
    
    @Nullable
    Geofence getGeofencePosition(long geofenceId);
}
