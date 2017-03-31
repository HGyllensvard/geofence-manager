package com.hgyllensvard.geofencemanager.di;


import com.hgyllensvard.geofencemanager.GeofenceManagerModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.ContextModule;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.GeofenceTransitionsIntentService;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.GeofenceTriggeredManager;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayGeofenceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                ContextModule.class,
                GeofenceModule.class,
                PlayGeofenceModule.class,
                GeofencePersistenceModule.class}
)
public interface GeofenceManagerComponent {

    com.hgyllensvard.geofencemanager.geofence.di.GeofenceManagerComponent plus(ActivityModule activityModule, GeofenceManagerModule geofenceManagerModule);

    void inject(GeofenceTransitionsIntentService geofenceTransitionsIntentService);

    GeofenceManager geofenceManager();

    GeofenceTriggeredManager geofenceTriggeredManager();

    SelectedGeofenceId selectedGeofence();

}
