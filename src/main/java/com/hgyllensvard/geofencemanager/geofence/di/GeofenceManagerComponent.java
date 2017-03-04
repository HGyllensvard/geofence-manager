package com.hgyllensvard.geofencemanager.geofence.di;

import com.hgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.hgyllensvard.geofencemanager.GeofenceManagerModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.edit.addGeofence.AddGeofenceView;
import com.hgyllensvard.geofencemanager.geofence.edit.displayGeofence.DisplayGeofenceView;
import com.hgyllensvard.geofencemanager.geofence.edit.editGeofence.EditGeofenceView;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(
        modules = {
                ActivityModule.class,
                GeofenceManagerModule.class}
)
public interface GeofenceManagerComponent {

    void inject(GeofenceManagerActivity activity);

    void inject(DisplayGeofenceView displayGeofenceView);

    void inject(EditGeofenceView editGeofenceView);

    void inject(AddGeofenceView addGeofenceView);
}
