package com.hgyllensvard.geofencemanager.geofence;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;

import timber.log.Timber;

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

        locationPermissionRequester.request()
                .subscribe(result -> {
                    Timber.d("Result: %s", result);
                }, throwable -> {
                    Timber.e(throwable, "Unexpected error");
                });
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
