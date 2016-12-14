package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import android.content.Context;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayGeofenceModule {

    private Context context;

    public PlayGeofenceModule(Context context) {
        this.context = context;
    }

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
    AddPlayGeofenceManager providesActivateGeofenceManager() {
        return new AddPlayGeofenceManager(context);
    }

    @PerActivity
    @Provides
    PlayApiManager providesPlayApiManager() {
        return new PlayApiManager(context);
    }

}
