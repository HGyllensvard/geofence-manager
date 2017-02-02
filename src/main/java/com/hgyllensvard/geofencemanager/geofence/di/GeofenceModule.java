package com.hgyllensvard.geofencemanager.geofence.di;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceViewManager;
import com.hgyllensvard.geofencemanager.geofence.map.MapCameraManager;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofenceModule {

    @Singleton
    @Provides
    GeofenceViewManager providesGeofenceMarkerManager(
            GeofenceManager geofenceManager,
            GeofenceMapOptions geofenceMapOptions
    ) {
        return new GeofenceViewManager(
                geofenceManager,
                geofenceMapOptions);
    }

    @Singleton
    @Provides
    GeofenceMapOptions providesGeofenceMapOptions(
            Context context
    ) {
        return GeofenceMapOptions.create()
                .strokeColor(ContextCompat.getColor(context, R.color.transparentRed))
                .fillColor(ContextCompat.getColor(context, R.color.transparentBlue))
                .build();
    }

    @Singleton
    @Provides
    GeofenceManager providesGeofenceManager(
            GeofenceRepository geofenceRepository,
            PlayServicesGeofenceManager playServicesGeofenceManager
    ) {
        return new GeofenceManager(
                geofenceRepository,
                playServicesGeofenceManager);
    }

    @Singleton
    @Provides
    MapCameraManager providesMapCameraManager(
            LocationManager locationManager
    ) {
        return new MapCameraManager(locationManager);
    }

    @Singleton
    @Provides
    LocationPermissionRequester providesLocationPermissionRequester(
            Context context
    ) {
        return new LocationPermissionRequester(context);
    }

    @Singleton
    @Provides
    LocationManager providesLocationManager(
            android.location.LocationManager locationManager,
            LocationPermissionRequester locationPermissionRequester
    ) {
        return new LocationManager(
                locationManager,
                locationPermissionRequester);
    }
}
