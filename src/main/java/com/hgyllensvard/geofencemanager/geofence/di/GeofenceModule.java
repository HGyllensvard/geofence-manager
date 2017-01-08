package com.hgyllensvard.geofencemanager.geofence.di;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.GeofenceManagerPresenter;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceManagerViewManager;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceMapOptions;

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
    GeofenceManagerPresenter providesGeofencePresenter(
            GeofenceManagerView geofenceView,
            GeofenceManager geofenceManager,
            LocationManager locationManager,
            LocationPermissionRequester locationPermissionRequester
    ) {
        return new GeofenceManagerPresenter(geofenceView,
                activity,
                locationManager,
                geofenceManager,
                locationPermissionRequester);
    }

    @PerActivity
    @Provides
    GeofenceManagerView providesGeofenceView(GeofenceMapOptions mapOptions) {
        return new GeofenceManagerViewManager(activity, mapOptions);
    }

    @PerActivity
    @Provides
    GeofenceMapOptions providesGeofenceMapOptions() {
        return GeofenceMapOptions.create(
                ContextCompat.getColor(activity, R.color.colorPrimary),
                ContextCompat.getColor(activity, R.color.colorPrimary));
    }

    @PerActivity
    @Provides
    LocationPermissionRequester providesLocationPermissionRequester() {
        return new LocationPermissionRequester(activity);
    }

    @PerActivity
    @Provides
    LocationManager providesLocationManager() {
        return (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
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
}
