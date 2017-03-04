package com.hgyllensvard.geofencemanager.geofence.di;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.GeofenceTriggeredManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofenceModule {

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
    GeofenceTriggeredManager providesGeofenceEventTriggeredManager(
            GeofenceManager geofenceManager
    ) {
        return new GeofenceTriggeredManager(geofenceManager);
    }

    @Singleton
    @Provides
    SelectedGeofence selectedGeofence() {
        return new SelectedGeofence();
    }
}
