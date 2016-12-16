package com.hgyllensvard.geofencemanager.geofence.persistence;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.DaggerGeofenceTestComponent;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;

import javax.inject.Inject;

public class RealmActivityTest extends AppCompatActivity {

    @Inject
    GeofenceRepository geofenceRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerGeofenceTestComponent.builder()
                .geofenceModule(new GeofenceModule(this))
                .geofencePersistenceModule(new GeofencePersistenceModule())
                .build()
                .inject(this);
    }
}
