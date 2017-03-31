package com.hgyllensvard.geofencemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.di.GeofenceModuleManager;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceManagerComponent;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.disposables.DisposableContainer;

public class GeofenceManagerActivity extends AppCompatActivity implements GeofenceManagerInjector {

    @Inject
    LocationManager locationPermissionRequester;

    private DisposableContainer disposableContainer;

    private GeofenceManagerComponent geofenceManagerComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposableContainer = new CompositeDisposable();

        setContentView(R.layout.activity_main);

//        ButterKnife.bind(this);

//        setSupportActionBar(toolbar);

        geofenceManagerComponent = GeofenceModuleManager.geofenceManagerComponent(this)
                .plus(new ActivityModule(this), new GeofenceManagerModule());

        geofenceManagerComponent.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayMap();
    }

    public GeofenceManagerComponent getGeofenceManagerComponent() {
        return geofenceManagerComponent;
    }

    private void displayMap() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, new SingleGeofenceFragment())
                .addToBackStack(null)
                .commit();
    }
}
