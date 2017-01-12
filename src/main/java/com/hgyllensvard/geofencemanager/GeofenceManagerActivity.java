package com.hgyllensvard.geofencemanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.addGeofence.AddGeofenceViews;
import com.hgyllensvard.geofencemanager.geofence.di.DaggerGeofenceComponent;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceModule;
import com.hgyllensvard.geofencemanager.geofence.displayGeofence.DisplayGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.displayGeofence.DisplayGeofenceViews;
import com.hgyllensvard.geofencemanager.geofence.editGeofence.EditGeofencePresenter;
import com.hgyllensvard.geofencemanager.geofence.editGeofence.EditGeofenceViews;
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

    @Inject
    DisplayGeofencePresenter displayGeofencePresenter;

    @Inject
    DisplayGeofenceViews displayGeofenceViews;

    @Inject
    AddGeofencePresenter addGeofencePresenter;

    @Inject
    AddGeofenceViews addGeofenceViews;

    @Inject
    EditGeofencePresenter editGeofencePresenter;

    @Inject
    EditGeofenceViews editGeofenceViews;

    private DisposableContainer disposableContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposableContainer = new CompositeDisposable();

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

        Disposable disposable = locationPermissionRequester.request()
                .subscribeOn(Schedulers.io())
                .subscribe(this::managePermissionResult,
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private void managePermissionResult(RequestPermissionResult permissionResult) {
        switch (permissionResult) {
            case DENIED:
                finish();
                break;
            case GRANTED:
                loadPresenters();
                break;
            default:
                throw new IllegalStateException("Unexpected argument");
        }
    }

    private void loadPresenters() {
        displayGeofencePresenter.bindView(displayGeofenceViews);
        addGeofencePresenter.bindView(addGeofenceViews);
        editGeofencePresenter.bindView(editGeofenceViews);
    }

    @Override
    protected void onPause() {
        super.onPause();

        displayGeofencePresenter.unbindView();
        addGeofencePresenter.unbindView();
        editGeofencePresenter.unbindView();
    }
}
