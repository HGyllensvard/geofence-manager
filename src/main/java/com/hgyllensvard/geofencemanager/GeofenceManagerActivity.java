package com.hgyllensvard.geofencemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.geofence.GeofenceViewPresenter;
import com.hgyllensvard.geofencemanager.geofence.di.DaggerGeofenceComponent;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayGeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceViews;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeofenceManagerActivity extends AppCompatActivity {

    @BindView(R2.id.geofence_toolbar)
    Toolbar toolbar;

    @Inject
    GeofenceViewPresenter geofenceViewPresenter;

    @Inject
    GeofenceViews geofenceViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar.setTitle("Activate geofences");

        setSupportActionBar(toolbar);

        DaggerGeofenceComponent.builder()
                .activityModule(new ActivityModule(this))
                .geofenceModule(new GeofenceModule())
                .playGeofenceModule(new PlayGeofenceModule())
                .geofencePersistenceModule(new GeofencePersistenceModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        geofenceViewPresenter.bindView(geofenceViews);
    }

    @Override
    protected void onPause() {
        super.onPause();

        geofenceViewPresenter.unbindView();
    }
}
