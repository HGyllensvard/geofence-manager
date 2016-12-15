package com.hgyllensvard.geofencemanager.geofence.persistence;


import android.content.Context;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class GeofencePersistenceModule {

    private final Context context;

    public GeofencePersistenceModule(Context context) {
        this.context = context;
    }

    @PerActivity
    @Provides
    GeofenceRepository providesGeofenceRepository(
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
