package com.henrikgyllensvard.geofencemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.henrikgyllensvard.geofencemanager.geofence.GeofenceManagerPresenter;

import javax.inject.Inject;

public class GeofenceManagerActivity extends AppCompatActivity {

    @Inject
    GeofenceManagerPresenter geofenceManagerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
