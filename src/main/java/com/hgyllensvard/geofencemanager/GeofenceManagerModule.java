package com.hgyllensvard.geofencemanager;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceViewManager;
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
            GeofenceViewManager geofenceViewManager
    ) {
        return new MapView(
                appCompatActivity,
                R.id.geofence_map_container,
                geofenceViewManager);
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

//    @PerActivity
//    @Provides
//    DisplayGeofencePresenter providesDisplayGeofencePresenter(
//            GeofenceViewManager geofenceViewManager,
//            MapCameraManager mapCameraManager
//    ) {
//        return new DisplayGeofencePresenter(
//                geofenceViewManager,
//                mapCameraManager);
//    }
//
//    @PerActivity
//    @Provides
//    AddGeofencePresenter providesAddMultipleGeofencePresenters(
//            GeofenceManager geofenceManager,
//            GeofenceMapOptions geofenceMapOptions
//    ) {
//        return new AddGeofencePresenter(
//                geofenceManager,
//                geofenceMapOptions,
//                selectedGeofenceId);
//    }

//    @PerActivity
//    @Provides
//    EditGeofencePresenter providesEditGeofencePresenter(
//            GeofenceManager geofenceManager
//    ) {
//        return new EditGeofencePresenter(geofenceManager);
//    }

    @PerActivity
    @Provides
    SelectedGeofence selectedGeofence() {
        return new SelectedGeofence();
    }
}
