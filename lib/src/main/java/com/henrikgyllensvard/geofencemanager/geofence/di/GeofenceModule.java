package com.henrikgyllensvard.geofencemanager.geofence.di;

import android.support.v7.app.AppCompatActivity;

import com.henrikgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.henrikgyllensvard.geofencemanager.geofence.GeofenceManagerPresenter;
import com.henrikgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.henrikgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;
import com.henrikgyllensvard.geofencemanager.geofence.view.GeofenceManagerViewManager;

import dagger.Module;
import dagger.Provides;

@Module
class GeofenceModule {

    private AppCompatActivity activity;

    public GeofenceModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    GeofenceManagerPresenter providesGeofencePresenter(
            GeofenceManagerView geofenceView,
            LocationPermissionRequester locationPermissionRequester
    ) {
        return new GeofenceManagerPresenter(geofenceView, locationPermissionRequester);
    }

    @PerActivity
    @Provides
    GeofenceManagerView providesGeofenceView() {
        return new GeofenceManagerViewManager(activity);
    }

    @PerActivity
    @Provides
    LocationPermissionRequester privatesLocationPermissionRequester() {
        return new LocationPermissionRequester(activity);
    }
}
