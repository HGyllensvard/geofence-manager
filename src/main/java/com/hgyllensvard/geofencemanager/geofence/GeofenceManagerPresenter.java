package com.hgyllensvard.geofencemanager.geofence;

import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.permission.RequestPermissionResult;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GeofenceManagerPresenter extends PresenterAdapter<GeofenceManagerView> {

    private final LocationPermissionRequester locationPermissionRequester;
    private final AppCompatActivity activity;

    private Disposable displayMap;

    public GeofenceManagerPresenter(
            GeofenceManagerView viewActions,
            AppCompatActivity activity,
            LocationPermissionRequester locationPermissionRequester
    ) {
        super(viewActions);

        this.activity = activity;
        this.locationPermissionRequester = locationPermissionRequester;
    }

    @Override
    public void onResume() {
        super.onResume();

        locationPermissionRequester.request()
                .subscribeOn(Schedulers.io())
                .subscribe(this::managePermissionResult,
                        throwable -> Timber.e(throwable, "Unexpected error"));
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void managePermissionResult(RequestPermissionResult permissionResult) {
        switch (permissionResult) {
            case DENIED:
                activity.finish();
                break;
            case GRANTED:
                loadMap();
                break;
            default:
                throw new IllegalStateException("Unexpected argument");
        }
    }

    private void loadMap() {
        displayMap = viewActions.displayMap()
                .subscribe();
    }
}
