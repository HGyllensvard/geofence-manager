package com.hgyllensvard.geofencemanager.geofence;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.RequestPermissionResult;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceViews;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GeofenceViewPresenter extends PresenterAdapter<GeofenceViews> {

    private static final int LONG_CLICK_DEBOUNCE_TIMER = 1;
    private static final TimeUnit LONG_CLICK_DEBOUNCE_TIMER_UNIT = TimeUnit.SECONDS;

    private final AppCompatActivity activity;
    private final LocationManager locationManager;
    private final GeofenceManager geofenceManager;
    private final GeofenceMapOptions mapOptions;
    private final MapCameraManager mapCameraManager;

    private final CompositeDisposable disposableContainer;

    private GeofenceData selectedGeofence;

    public GeofenceViewPresenter(
            AppCompatActivity activity,
            LocationManager locationManager,
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions,
            MapCameraManager mapCameraManager
    ) {
        this.activity = activity;
        this.locationManager = locationManager;
        this.geofenceManager = geofenceManager;
        this.mapOptions = mapOptions;
        this.mapCameraManager = mapCameraManager;

        disposableContainer = new CompositeDisposable();
    }

    @Override
    public void bindView(@NonNull GeofenceViews geofenceViews) {
        super.bindView(geofenceViews);

        Disposable disposable = locationManager.request()
                .subscribeOn(Schedulers.io())
                .subscribe(this::managePermissionResult,
                        Timber::e);

        disposableContainer.add(disposable);
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
        Disposable disposable = viewActions.displayMap()
                .doOnSuccess(ignored -> zoomToUserPosition())
                .observeOn(Schedulers.io())
                .doOnSuccess(ignored -> subscribeLongClick())
                .doOnSuccess(ignored -> subscribeExistingGeofences())
                .doOnSuccess(ignored -> subscribeSelectedGeofence())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                        },
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private void subscribeExistingGeofences() {
        Disposable disposable = geofenceManager.observeGeofences()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(geofenceDatas -> viewActions.displayGeofences(geofenceDatas),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private void subscribeLongClick() {
        Disposable disposable = viewActions.observerLongClick()
                .observeOn(Schedulers.io())
                .debounce(LONG_CLICK_DEBOUNCE_TIMER, LONG_CLICK_DEBOUNCE_TIMER_UNIT)
                .subscribe(this::addGeofence,
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private void addGeofence(LatLng latLng) {
        disposableContainer.add(
                geofenceManager.addGeofence(createNewGeofence(latLng))
                        .subscribeOn(Schedulers.io())
                        .subscribe(geofenceData -> {
                                },
                                Timber::e));
    }

    private void subscribeSelectedGeofence() {
        Disposable disposable = viewActions.observeGeofenceSelected()
                .subscribe(newlySelectedGeofence -> {
                    if (newlySelectedGeofence.isGeofenceSelected()) {
                        selectedGeofence = newlySelectedGeofence.selectedGeofence();
                        viewActions.displaySelectedGeofenceOptions();
                    } else {
                        selectedGeofence = null;
                        viewActions.hideSelectedGeofenceOptions();
                    }
                }, Timber::e);

        disposableContainer.add(disposable);
    }

    private void renameSelectedGeofence(
            GeofenceData geofence,
            String newName
    ) {
        Disposable disposable = geofenceManager.updateGeofence(geofence, geofence.withName(newName))
                .subscribe(updatedGeofence -> Timber.v("Updated geofence to: %s", updatedGeofence),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private GeofenceData createNewGeofence(LatLng latLng) {
        return GeofenceData.create(mapOptions.geofenceCreatedName(), latLng, mapOptions.geofenceCreatedRadius());
    }

    private void zoomToUserPosition() throws SecurityException {
        viewActions.animateCameraTo(mapCameraManager.userPosition());
    }

    @VisibleForTesting
    CompositeDisposable getSubscriptions() {
        return disposableContainer;
    }
}
