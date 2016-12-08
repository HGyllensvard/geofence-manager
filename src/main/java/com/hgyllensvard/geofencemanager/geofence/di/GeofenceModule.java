package com.hgyllensvard.geofencemanager.geofence.di;

import android.content.Context;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.GeofenceManagerPresenter;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceManagerViewManager;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofenceModule {

    private AppCompatActivity activity;

    public GeofenceModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    GeofenceManagerPresenter providesGeofencePresenter(
            GeofenceManagerView geofenceView,
            LocationManager locationManager,
            LocationPermissionRequester locationPermissionRequester
    ) {
        return new GeofenceManagerPresenter(geofenceView,
                activity,
                locationManager,
                locationPermissionRequester);
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

    @PerActivity
    @Provides
    LocationManager privatesLocationManager() {
        return (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }
}
