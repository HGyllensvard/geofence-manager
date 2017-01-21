package com.hgyllensvard.geofencemanager.geofence.editGeofence;


import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;

import io.reactivex.Observable;

public interface EditGeofenceViews extends View {

    Observable<Boolean> displayMap();

    Observable<Long> observeGeofenceSelected();

    Observable<Integer> observeCameraStartedMoving();

    void hideSelectedGeofenceOptions();

    void displaySelectedGeofenceOptions();

    Observable<Boolean> observeRenameGeofence();

    Observable<Boolean> observeDeleteGeofence();

    void displayRenameGeofence(String name);

    void removeMarkerFromMap(long selectedGeofenceId);
}
