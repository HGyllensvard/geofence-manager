package com.hgyllensvard.geofencemanager.geofence.editGeofence;


import com.google.android.gms.maps.model.Marker;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface EditGeofenceViews extends View {

    Flowable<Boolean> displayMap();

    Flowable<Marker> observeMarkerSelected();

    Flowable<Integer> observeCameraStartedMoving();

    void hideSelectedGeofenceOptions();

    void displaySelectedGeofenceOptions();

    Flowable<Boolean> observeRenameGeofence();

    Flowable<Boolean> observeDeleteGeofence();
}
