package com.hgyllensvard.geofencemanager;


import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddMultipleGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddSingleGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.displayGeofence.DisplayGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.editGeofence.EditGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceViewManager;
import com.hgyllensvard.geofencemanager.geofence.map.MapCameraManager;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

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
    AddMultipleGeofencePresenter providesAddMultipleGeofencePresenters(
            GeofenceManager geofenceManager,
            GeofenceMapOptions geofenceMapOptions
    ) {
        return new AddMultipleGeofencePresenter(
                geofenceManager,
                geofenceMapOptions);
    }

    @PerActivity
    @Provides
    AddSingleGeofencePresenter providesAddSinglaGeofencePresenter(
            GeofenceManager geofenceManager,
            GeofenceMapOptions geofenceMapOptions
    ) {
        return new AddSingleGeofencePresenter(
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
}
