package com.hgyllensvard.geofencemanager.geofence.di;

import android.app.Activity;
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

import java.util.Map;

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
    Activity providesActivity() {
        return activity;
    }

    @PerActivity
    @Provides
    Context providesContext() {
        return activity;
    }

    @PerActivity
    @Provides
    GeofenceViewPresenter providesGeofencePresenter(
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
    GeofenceViews providesGeofenceView(GeofenceMapOptions mapOptions) {
        return new GeofenceViewsManager(activity, mapOptions);
    }

    @PerActivity
    @Provides
    GeofenceMapOptions providesGeofenceMapOptions() {
        return GeofenceMapOptions.create(
                ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorPrimary),
                100);
    }

    @PerActivity
    @Provides
    LocationPermissionRequester providesLocationPermissionRequester() {
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
    android.location.LocationManager providesAndroidLocationManager() {
        return (android.location.LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
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
