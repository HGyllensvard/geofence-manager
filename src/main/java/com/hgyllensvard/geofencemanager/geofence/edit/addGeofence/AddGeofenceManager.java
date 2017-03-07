package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceActionResult;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddGeofenceManager {

    private final GeofenceManager geofenceManager;
    private final GeofenceMapOptions mapOptions;
    private final SelectedGeofence selectedGeofence;

    @Inject
    public AddGeofenceManager(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions,
            SelectedGeofence selectedGeofence
    ) {
        this.geofenceManager = geofenceManager;
        this.mapOptions = mapOptions;
        this.selectedGeofence = selectedGeofence;
    }

    public Single<GeofenceActionResult> addGeofence(LatLng geofenceLatLng) {
        return Single.just(geofenceLatLng)
                .flatMap(latLng -> shouldAddNewGeofence()
                        .doOnSuccess(this::logIfNotAddingGeofence)
                        .map(ignored -> latLng))
                .flatMap(this::storeGeofence);
    }
    
    private Single<Boolean> shouldAddNewGeofence() {
        return Single.fromCallable(selectedGeofence::selectedGeofence)
                .flatMap(geofenceManager::exist);
    }

    private void logIfNotAddingGeofence(boolean isAddingGeofence) {
        if (!isAddingGeofence) {
            Timber.w("A geofence is already selected, will therefore not add new geofence");
        }
    }

    private Single<GeofenceActionResult> storeGeofence(LatLng latLng) {
        return geofenceManager.addGeofence(createNewGeofence(latLng))
                .subscribeOn(Schedulers.io())
                .doOnSuccess(this::setSelectedGeofence);
    }

    private Geofence createNewGeofence(LatLng latLng) {
        return Geofence.create(mapOptions.geofenceCreatedName(), latLng, mapOptions.geofenceCreatedRadius(), true);
    }

    private void setSelectedGeofence(GeofenceActionResult geofenceActionResult) {
        if (geofenceActionResult.success()) {
            Geofence geofence = geofenceActionResult.geofence();

            if (geofence == null) {
                Timber.w("Geofence is null, but shouldn't be");
                return;
            }

            selectedGeofence.updatedSelectedGeofence(geofence.id());
        }
    }
}
