package com.hgyllensvard.geofencemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.geofence.di.DaggerGeofenceComponent;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceComponent;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.permission.RequestPermissionResult;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofencePersistenceModule;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayGeofenceModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GeofenceManagerActivity extends AppCompatActivity {

    @BindView(R2.id.geofence_toolbar)
    Toolbar toolbar;

    @Inject
    LocationPermissionRequester locationPermissionRequester;

    private DisposableContainer disposableContainer;

    private GeofenceComponent geofenceComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposableContainer = new CompositeDisposable();

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar.setTitle("Activate geofences");

        setSupportActionBar(toolbar);

        geofenceComponent = DaggerGeofenceComponent.builder()
                .activityModule(new ActivityModule(this))
                .geofenceModule(new GeofenceModule())
                .playGeofenceModule(new PlayGeofenceModule())
                .geofencePersistenceModule(new GeofencePersistenceModule())
                .build();

        geofenceComponent.inject(this);
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

    public GeofenceComponent getGeofenceComponent() {
        return geofenceComponent;
    }

    private void managePermissionResult(RequestPermissionResult permissionResult) {
        switch (permissionResult) {
            case DENIED:
                finish();
                break;
            case GRANTED:
                Timber.v("Location permission granted, continuing");
                break;
            default:
                throw new IllegalStateException("Unexpected argument");
        }
    }
}
