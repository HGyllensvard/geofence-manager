package com.hgyllensvard.geofencemanager.di;


import android.content.Context;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.ContextModule;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayGeofenceModule;

public class GeofenceModuleManager {

    private static GeofenceModuleManager moduleManager;

    private static GeofenceManagerComponent sGeofenceManagerComponent;

    private GeofenceModuleManager(Context context) {
        sGeofenceManagerComponent = DaggerGeofenceManagerComponent.builder()
                .contextModule(new ContextModule(context))
                .geofenceModule(new GeofenceModule())
                .geofencePersistenceModule(new GeofencePersistenceModule())
                .playGeofenceModule(new PlayGeofenceModule())
                .build();
    }

    public static GeofenceManagerComponent geofenceManagerComponent(Context context) {
        generateGeofenceComponent(context);

        return sGeofenceManagerComponent;
    }

    private static void generateGeofenceComponent(Context context) {
        if (moduleManager == null) {
            moduleManager = new GeofenceModuleManager(context.getApplicationContext());
        }
    }
}
