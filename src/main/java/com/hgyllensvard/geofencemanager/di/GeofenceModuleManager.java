package com.hgyllensvard.geofencemanager.di;


import android.content.Context;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.ContextModule;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayGeofenceModule;

public class GeofenceModuleManager {

    private static GeofenceModuleManager moduleManager;

    private static GeofenceComponent sGeofenceComponent;

    private GeofenceModuleManager(Context context) {
        sGeofenceComponent = DaggerGeofenceComponent.builder()
                .contextModule(new ContextModule(context))
                .geofenceModule(new GeofenceModule())
                .geofencePersistenceModule(new GeofencePersistenceModule())
                .playGeofenceModule(new PlayGeofenceModule())
                .build();
    }

    public static GeofenceComponent geofenceComponent(Context context) {
        generateGeofenceComponent(context);

        return sGeofenceComponent;
    }

    private static void generateGeofenceComponent(Context context) {
        if (moduleManager == null) {
            moduleManager = new GeofenceModuleManager(context.getApplicationContext());
        }
    }
}
