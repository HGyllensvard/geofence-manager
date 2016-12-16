package com.hgyllensvard.geofencemanager;


import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.persistence.RealmActivityTest;

import dagger.Component;

@PerActivity
@Component(
        modules = {GeofenceModule.class, GeofencePersistenceModule.class}
)
public interface GeofenceTestComponent {

    void inject(RealmActivityTest activity);
}
