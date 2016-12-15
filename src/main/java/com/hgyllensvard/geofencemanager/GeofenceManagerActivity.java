package com.hgyllensvard.geofencemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hgyllensvard.geofencemanager.geofence.GeofenceManagerPresenter;
import com.hgyllensvard.geofencemanager.geofence.di.DaggerGeofenceComponent;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayGeofenceModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeofenceManagerActivity extends AppCompatActivity {

    @BindView(R2.id.geofence_toolbar)
    Toolbar toolbar;

    @Inject
    GeofenceManagerPresenter geofenceManagerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar.setTitle("Activate geofences");

        setSupportActionBar(toolbar);

        DaggerGeofenceComponent.builder()
                .geofenceModule(new GeofenceModule(this))
                .playGeofenceModule(new PlayGeofenceModule(this))
                .geofencePersistenceModule(new GeofencePersistenceModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        geofenceManagerPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        geofenceManagerPresenter.onPause();
    }
}
