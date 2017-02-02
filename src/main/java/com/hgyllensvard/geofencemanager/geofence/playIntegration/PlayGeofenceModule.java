package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import android.content.Context;

import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PlayGeofenceModule {

    @Singleton
    @Provides
    PlayServicesGeofenceManager providesPlayGeofenceManager(
            AddPlayGeofenceManager addPlayGeofenceManager,
            RemovePlayGeofenceManager removePlayGeofenceManager
    ) {
        return new PlayServicesGeofenceManager(
                addPlayGeofenceManager,
                removePlayGeofenceManager);
    }

    @Singleton
    @Provides
    RemovePlayGeofenceManager providesRemoveGeofenceManager(
            GeofencingApi geofencingApi,
            PlayApiManager playApiManager
    ) {
        return new RemovePlayGeofenceManager(geofencingApi, playApiManager);
    }

    @Singleton
    @Provides
    AddPlayGeofenceManager providesActivateGeofenceManager(
            Context context,
            GeofencingApi geofencingApi,
            PlayApiManager playApiManager
    ) {
        return new AddPlayGeofenceManager(context, geofencingApi, playApiManager);
    }

    @Singleton
    @Provides
    PlayApiManager providesPlayApiManager(
            Context context
    ) {
        return new PlayApiManager(context);
    }

    @Singleton
    @Provides
    GeofencingApi providesGeofenceApi() {
        return LocationServices.GeofencingApi;
    }
}
