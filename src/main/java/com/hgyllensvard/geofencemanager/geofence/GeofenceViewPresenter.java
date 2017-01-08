package com.hgyllensvard.geofencemanager.geofence;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationPermissionRequester;
import com.hgyllensvard.geofencemanager.geofence.permission.RequestPermissionResult;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceViews;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GeofenceViewPresenter extends PresenterAdapter<GeofenceViews> {

    private final AppCompatActivity activity;
    private final LocationManager locationManager;
    private final GeofenceManager geofenceManager;
    private final LocationPermissionRequester locationPermissionRequester;

    private final CompositeDisposable disposableContainer;

    public GeofenceViewPresenter(
            AppCompatActivity activity,
            LocationManager locationManager,
            GeofenceManager geofenceManager,
            LocationPermissionRequester locationPermissionRequester
    ) {
        this.activity = activity;
        this.locationManager = locationManager;
        this.geofenceManager = geofenceManager;
        this.locationPermissionRequester = locationPermissionRequester;

        disposableContainer = new CompositeDisposable();
    }

    @Override
    public void bindView(@NonNull GeofenceViews geofenceViews) {
        super.bindView(geofenceViews);

        disposableContainer.add(
                locationPermissionRequester.request()
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::managePermissionResult,
                                Timber::e));
    }

    @Override
    public void unbindView() {
        super.unbindView();

        disposableContainer.clear();
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
        disposableContainer.add(
                viewActions.displayMap()
                        .doOnSuccess(ignored -> zoomToUserPosition())
                        .observeOn(Schedulers.io())
                        .doOnSuccess(ignored -> subscribeLongClick())
                        .doOnSuccess(ignored -> subscribeExistingGeofences())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(ignored -> {
                                },
                                Timber::e));
    }

    private void subscribeExistingGeofences() {
        disposableContainer.add(
                geofenceManager.observeGeofences()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(geofenceDatas -> viewActions.displayGeofences(geofenceDatas),
                                Timber::e)
        );
    }

    private void subscribeLongClick() {
        disposableContainer.add(
                viewActions.observerLongClick()
                        .observeOn(Schedulers.io())
                        .debounce(1, TimeUnit.SECONDS)
                        .subscribe(this::addGeofence,
                                Timber::e));
    }

    private void addGeofence(LatLng latLng) {
        disposableContainer.add(
                geofenceManager.addGeofence("GeofenceName", latLng, 100)
                        .subscribeOn(Schedulers.io())
                        .subscribe(geofenceData -> {
                                },
                                Timber::e));
    }

    private void zoomToUserPosition() throws SecurityException {
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(16)
                    .build();
            viewActions.animateCameraTo(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
