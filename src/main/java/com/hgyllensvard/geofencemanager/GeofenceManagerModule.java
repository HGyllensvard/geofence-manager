package com.hgyllensvard.geofencemanager;


import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddGeofencePresenter;
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
}
