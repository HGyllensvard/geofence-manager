package com.hgyllensvard.geofencemanager.geofence.di;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.displayGeofence.DisplayGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.editGeofence.EditGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceViewManager;
import com.hgyllensvard.geofencemanager.geofence.map.MapCameraManager;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofenceModule {

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

    @PerActivity
    @Provides
    DisplayGeofencePresenter providesDisplayGeofencePresenter(
            GeofenceViewManager geofenceViewManager,
            MapCameraManager mapCameraManager
    ) {
        return new DisplayGeofencePresenter(
                geofenceViewManager,
                mapCameraManager);
    }

    @PerActivity
    @Provides
    GeofenceViewManager providesGeofenceMarkerManager(
            GeofenceManager geofenceManager,
            GeofenceMapOptions geofenceMapOptions
    ) {
        return new GeofenceViewManager(
                geofenceManager,
                geofenceMapOptions);
    }

    @PerActivity
    @Provides
    AddGeofencePresenter providesAddGeofencePresenter(
            GeofenceManager geofenceManager,
            GeofenceMapOptions geofenceMapOptions
    ) {
        return new AddGeofencePresenter(
                geofenceManager,
                geofenceMapOptions);
    }

    @PerActivity
    @Provides
    EditGeofencePresenter providesEditGeofencePresenter(
            GeofenceManager geofenceManager
    ) {
        return new EditGeofencePresenter(geofenceManager);
    }

    @PerActivity
    @Provides
    GeofenceMapOptions providesGeofenceMapOptions(
            Context context
    ) {
        return GeofenceMapOptions.create()
                .strokeColor(ContextCompat.getColor(context, R.color.transparentRed))
                .fillColor(ContextCompat.getColor(context, R.color.transparentBlue))
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
