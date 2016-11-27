package com.hgyllensvard.geofencemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.geofence.GeofenceManagerPresenter;

import javax.inject.Inject;

public class GeofenceManagerActivity extends AppCompatActivity {

    @Inject
    GeofenceManagerPresenter geofenceManagerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
