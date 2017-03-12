package com.hgyllensvard.geofencemanager;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceViewMapsManager;
import com.hgyllensvard.geofencemanager.geofence.edit.map.MapView;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofenceManagerModule {

    @Provides
    @PerActivity
    MapView providesMapView(
            AppCompatActivity appCompatActivity,
            LocationManager locationManager,
            GeofenceViewMapsManager geofenceMapManagers
    ) {
        return new MapView(
                appCompatActivity,
                R.id.geofence_map_container,
                locationManager,
                geofenceMapManagers);
    }

    @Provides
    @PerActivity
    GeofenceViewMapsManager geofenceMapManagers() {
        return new GeofenceViewMapsManager();
    }

    @Provides
    @PerActivity
    LocationPermissionRequester providesLocationPermissionRequester(
            AppCompatActivity activity
    ) {
        return new LocationPermissionRequester(activity);
    }

    @Provides
    @PerActivity
    LocationManager providesLocationManager(
            android.location.LocationManager locationManager,
            LocationPermissionRequester locationPermissionRequester
    ) {
        return new LocationManager(
                locationManager,
                locationPermissionRequester);
    }

    @PerActivity
    @Provides
    android.location.LocationManager providesAndroidLocationManager(Context context) {
        return (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
