package com.henrikgyllensvard.geofencemanager.geofence;

import com.henrikgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.henrikgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.henrikgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;

public class GeofenceManagerPresenter extends PresenterAdapter<GeofenceManagerView> {

    private final LocationPermissionRequester locationPermissionRequester;

    public GeofenceManagerPresenter(
            GeofenceManagerView viewActions,
            LocationPermissionRequester locationPermissionRequester
    ) {
        super(viewActions);

        this.locationPermissionRequester = locationPermissionRequester;
    }

    @Override
    public void onResume() {
        super.onResume();

        locationPermissionRequester.request();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
