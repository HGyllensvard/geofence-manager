package com.hgyllensvard.geofencemanager.geofence;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.permission.RequestPermissionResult;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GeofenceManagerPresenter extends PresenterAdapter<GeofenceManagerView> {

    private final LocationPermissionRequester locationPermissionRequester;
    private final LocationManager locationManager;
    private final AppCompatActivity activity;

    private Disposable displayMap;

    public GeofenceManagerPresenter(
            GeofenceManagerView viewActions,
            AppCompatActivity activity,
            LocationManager locationManager,
            LocationPermissionRequester locationPermissionRequester
    ) {
        super(viewActions);

        this.activity = activity;
        this.locationManager = locationManager;
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
                .doOnSuccess(aBoolean -> zoomToUserPosition())
                .subscribe();
    }

    private void zoomToUserPosition() {
        Criteria criteria = new Criteria();

        try {
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(16)
                        .build();
                viewActions.animateCameraTo(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        } catch (SecurityException e) {
            Timber.e(e, "Could not zoom to users position, the location permission should already have been given");
        }
    }
}
