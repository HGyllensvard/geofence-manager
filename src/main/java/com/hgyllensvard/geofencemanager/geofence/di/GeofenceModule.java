package com.hgyllensvard.geofencemanager.geofence.di;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.GeofenceViewPresenter;
import com.hgyllensvard.geofencemanager.geofence.MapCameraManager;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceViews;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceViewsManager;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofenceModule {

    @PerActivity
    @Provides
    GeofenceViewPresenter providesGeofencePresenter(
            AppCompatActivity activity,
            GeofenceManager geofenceManager,
            LocationManager locationManager,
            GeofenceMapOptions mapOptions,
            MapCameraManager mapCameraManager
    ) {
        return new GeofenceViewPresenter(
                activity,
                locationManager,
                geofenceManager,
                mapOptions,
                mapCameraManager);
    }

    @PerActivity
    @Provides
    GeofenceViews providesGeofenceView(
            AppCompatActivity activity,
            GeofenceMapOptions mapOptions
    ) {
        return new GeofenceViewsManager(activity, mapOptions);
    }

    @PerActivity
    @Provides
    GeofenceMapOptions providesGeofenceMapOptions(
            Context context
    ) {
        return GeofenceMapOptions.create()
                .strokeColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .fillColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .build();
    }

    @PerActivity
    @Provides
    LocationPermissionRequester providesLocationPermissionRequester(
            AppCompatActivity activity
    ) {
        return new LocationPermissionRequester(activity);
    }

    @PerActivity
    @Provides
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
    GeofenceManager providesGeofenceManager(
            GeofenceRepository geofenceRepository,
            PlayServicesGeofenceManager playServicesGeofenceManager
    ) {
        return new GeofenceManager(
                geofenceRepository,
                playServicesGeofenceManager);
    }

    @PerActivity
    @Provides
    MapCameraManager providesMapCameraManager(
            LocationManager locationManager
    ) {
        return new MapCameraManager(locationManager);
    }
}
