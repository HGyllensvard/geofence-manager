package com.hgyllensvard.geofencemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.di.GeofenceModuleManager;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceManagerComponent;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.RequestPermissionResult;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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

//        toolbar.setTitle("Activate geofences");

//        setSupportActionBar(toolbar);

        geofenceManagerComponent = GeofenceModuleManager.geofenceManagerComponent(this)
                .plus(new ActivityModule(this), new GeofenceManagerModule());

        geofenceManagerComponent.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Disposable disposable = locationPermissionRequester.request()
                .subscribeOn(Schedulers.io())
                .subscribe(this::managePermissionResult,
                        Timber::e);

        disposableContainer.add(disposable);
    }

    public GeofenceManagerComponent getGeofenceManagerComponent() {
        return geofenceManagerComponent;
    }

    private void managePermissionResult(RequestPermissionResult permissionResult) {
        switch (permissionResult) {
            case DENIED:
                finish();
                break;
            case GRANTED:
                Timber.v("Location permission granted, continuing");
                displayMap();
                break;
            default:
                throw new IllegalStateException("Unexpected argument");
        }
    }

    private void displayMap() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, new SingleGeofenceFragment())
                .addToBackStack(null)
                .commit();
    }
}
