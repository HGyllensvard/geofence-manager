package com.hgyllensvard.geofencemanager.geofence.persistence;


import android.content.Context;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofencePersistenceModule {

    @PerActivity
    @Provides
    GeofenceRepository providesGeofenceRepository(
            Context context,
            GeofenceMapper geofenceMapper
    ) {
        return new GeofenceRepository(context, geofenceMapper);
    }

    @PerActivity
    @Provides
    GeofenceMapper providesGeofenceMapper() {
        return new GeofenceMapper();
    }
}
