package com.henrikgyllensvard.geofencemanager.geofence.di;

import android.support.v7.app.AppCompatActivity;

import com.henrikgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.henrikgyllensvard.geofencemanager.geofence.GeofenceManagerPresenter;
import com.henrikgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;
import com.henrikgyllensvard.geofencemanager.geofence.view.GeofenceManagerViewManager;

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
    public GeofenceManagerPresenter providesGeofencePresenter(
            GeofenceManagerView geofenceView) {
        return new GeofenceManagerPresenter(geofenceView);
    }

    @PerActivity
    @Provides
    public GeofenceManagerView providesGeofenceView() {
        return new GeofenceManagerViewManager(activity);
    }
}
