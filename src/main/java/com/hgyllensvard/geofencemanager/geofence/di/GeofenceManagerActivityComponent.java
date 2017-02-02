package com.hgyllensvard.geofencemanager.geofence.di;

import com.hgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.hgyllensvard.geofencemanager.GeofenceManagerModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddGeofenceView;
import com.hgyllensvard.geofencemanager.geofence.displayGeofence.DisplayGeofenceView;
import com.hgyllensvard.geofencemanager.geofence.editGeofence.EditGeofenceView;

import dagger.Component;
import dagger.Subcomponent;

@PerActivity
@Subcomponent(
        modules = {
                ActivityModule.class,
                GeofenceManagerModule.class}
)
public interface GeofenceManagerActivityComponent {

    void inject(GeofenceManagerActivity activity);

    void inject(EditGeofenceView editGeofenceView);

    void inject(AddGeofenceView addGeofenceView);

    void inject(DisplayGeofenceView displayGeofenceView);
}
