package com.hgyllensvard.geofencemanager.geofence.di;

import com.hgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayGeofenceModule;

import dagger.Component;

@PerActivity
@Component(
        modules = {GeofenceModule.class, PlayGeofenceModule.class}
)
public interface GeofenceComponent {

    void inject(GeofenceManagerActivity activity);
}
