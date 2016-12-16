package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import android.content.Context;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayGeofenceModule {

    @PerActivity
    @Provides
    PlayServicesGeofenceManager providesPlayGeofenceManager(
            PlayApiManager playApiManager,
            AddPlayGeofenceManager addPlayGeofenceManager,
            RemovePlayGeofenceManager removePlayGeofenceManager
    ) {
        return new PlayServicesGeofenceManager(
                playApiManager,
                addPlayGeofenceManager,
                removePlayGeofenceManager);
    }

    @PerActivity
    @Provides
    RemovePlayGeofenceManager providesRemoveGeofenceManager() {
        return new RemovePlayGeofenceManager();
    }

    @PerActivity
    @Provides
    AddPlayGeofenceManager providesActivateGeofenceManager(
            Context context
    ) {
        return new AddPlayGeofenceManager(context);
    }

    @PerActivity
    @Provides
    PlayApiManager providesPlayApiManager(
            Context context
    ) {
        return new PlayApiManager(context);
    }

}
