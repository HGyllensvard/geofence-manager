package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import android.content.Context;

import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.LocationServices;
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
    RemovePlayGeofenceManager providesRemoveGeofenceManager(
            GeofencingApi geofencingApi
    ) {
        return new RemovePlayGeofenceManager(geofencingApi);
    }

    @PerActivity
    @Provides
    AddPlayGeofenceManager providesActivateGeofenceManager(
            Context context,
            GeofencingApi geofencingApi
    ) {
        return new AddPlayGeofenceManager(context, geofencingApi);
    }

    @PerActivity
    @Provides
    PlayApiManager providesPlayApiManager(
            Context context
    ) {
        return new PlayApiManager(context);
    }

    @PerActivity
    @Provides
    GeofencingApi providesGeofenceApi() {
        return LocationServices.GeofencingApi;
    }
}
