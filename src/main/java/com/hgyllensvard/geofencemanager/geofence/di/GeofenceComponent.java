package com.hgyllensvard.geofencemanager.geofence.di;

import com.hgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.ContextModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayGeofenceModule;

import dagger.Component;

@PerActivity
@Component(
        modules = {
                ActivityModule.class,
                GeofenceModule.class,
                PlayGeofenceModule.class,
                GeofencePersistenceModule.class}
)
public interface GeofenceComponent {

    void inject(GeofenceManagerActivity activity);
}
