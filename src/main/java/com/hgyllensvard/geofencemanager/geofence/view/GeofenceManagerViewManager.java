package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.v7.app.AppCompatActivity;

public class GeofenceManagerViewManager implements GeofenceManagerView {

    private final AppCompatActivity activity;

    public GeofenceManagerViewManager(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void destroy() {

    }
}
