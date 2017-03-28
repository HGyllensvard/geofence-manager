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

    Single<GeofenceActionResult> attemptAddGeofence(LatLng geofenceLatLng) {
        return Single.just(selectedGeofence.selectedGeofence())
                .flatMap(selectedGeofenceId -> {
                    if (selectedGeofenceId == Geofence.NO_ID) {
                        return storeGeofence(geofenceLatLng);
                    }

                    Timber.w("Selected Geofence already exists: %s, will therefore not add new geofence", selectedGeofenceId);
                    return Single.just(GeofenceActionResult.failure(new GeofenceAlreadySelectedError(selectedGeofenceId)));
                });
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
                throw new IllegalStateException("Geofence is null in result but shouldn't be");
            }

            selectedGeofence.updatedSelectedGeofence(geofence.id());
        }
    }
}
