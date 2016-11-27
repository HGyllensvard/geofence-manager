package com.hgyllensvard.geofencemanager.geofence.di;

import com.hgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(
        modules = GeofenceModule.class
)
public interface GeofenceComponent {

    void inject(GeofenceManagerActivity activity);
}
