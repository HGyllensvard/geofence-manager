package com.hgyllensvard.geofencemanager.geofence.di;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.map.MapCameraManager;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddGeofenceView;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddGeofenceViews;
import com.hgyllensvard.geofencemanager.geofence.displayGeofence.DisplayGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.displayGeofence.DisplayGeofenceView;
import com.hgyllensvard.geofencemanager.geofence.displayGeofence.DisplayGeofenceViews;
import com.hgyllensvard.geofencemanager.geofence.editGeofence.EditGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.editGeofence.EditGeofenceView;
import com.hgyllensvard.geofencemanager.geofence.editGeofence.EditGeofenceViews;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceViewManager;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofenceModule {

    @PerActivity
    @Provides
    DisplayGeofenceViews providesGeofenceView(
            MapView mapView
    ) {
        return new DisplayGeofenceView(mapView);
    }

    @PerActivity
    @Provides
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
            GeofenceManager geofenceManager,
            MapCameraManager mapCameraManager
    ) {
        return new DisplayGeofencePresenter(
                geofenceManager,
                mapCameraManager);
    }

    @PerActivity
    @Provides
    GeofenceViewManager providesGeofenceMarkerManager(
            GeofenceMapOptions geofenceMapOptions
    ) {
        return new GeofenceViewManager(geofenceMapOptions);
    }

    @PerActivity
    @Provides
    AddGeofenceViews providesAddGeofenceViews(
            MapView mapView
    ) {
        return new AddGeofenceView(mapView);
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
    EditGeofenceViews providesEditGeofenceViews(
            AppCompatActivity activity,
            MapView mapView
    ) {
        return new EditGeofenceView(activity, mapView);
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
                .strokeColor(ContextCompat.getColor(context, R.color.colorAccent))
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
