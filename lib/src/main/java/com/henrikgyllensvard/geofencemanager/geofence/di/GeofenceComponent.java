package com.henrikgyllensvard.geofencemanager.geofence.di;

import com.henrikgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.henrikgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;

import dagger.Component;

@PerActivity
@Component(
        modules = GeofenceModule.class
)
public interface GeofenceComponent {

    void inject(GeofenceManagerActivity activity);
}
