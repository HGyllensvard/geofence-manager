package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceActionResult;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceIdState;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddGeofenceManager {

    private final GeofenceManager geofenceManager;
    private final GeofenceMapOptions mapOptions;
    private final SelectedGeofenceId selectedGeofenceId;

    @Inject
    public AddGeofenceManager(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions,
            SelectedGeofenceId selectedGeofenceId
    ) {
        this.geofenceManager = geofenceManager;
        this.mapOptions = mapOptions;
        this.selectedGeofenceId = selectedGeofenceId;
    }

    Single<GeofenceActionResult> attemptAddGeofence(LatLng geofenceLatLng) {
        return Single.just(selectedGeofenceId.selectedGeofenceState())
                .flatMap(selectedGeofenceState -> {
                    if (!selectedGeofenceState.isGeofenceSelected()) {
                        return storeGeofence(geofenceLatLng);
                    }

                    return noGeofenceAdded(selectedGeofenceState);
                });
    }

    private SingleSource<? extends GeofenceActionResult> noGeofenceAdded(SelectedGeofenceIdState selectedGeofenceIdState) {
        Timber.w("Selected Geofence already exists: %s, will therefore not add new geofence", selectedGeofenceIdState.geofenceId());
        return Single.just(GeofenceActionResult.failure(new GeofenceAlreadySelectedError(selectedGeofenceIdState.geofenceId())));
    }

    private Single<GeofenceActionResult> storeGeofence(LatLng latLng) {
        return geofenceManager.addGeofence(createNewGeofence(latLng))
                .subscribeOn(Schedulers.io())
                .doOnSuccess(this::setSelectedGeofenceId);
    }

    private Geofence createNewGeofence(LatLng latLng) {
        return Geofence.create(mapOptions.geofenceCreatedName(), latLng, mapOptions.geofenceCreatedRadius(), true);
    }

    private void setSelectedGeofenceId(GeofenceActionResult geofenceActionResult) {
        if (geofenceActionResult.success()) {
            Geofence geofence = geofenceActionResult.geofence();

            if (geofence == null) {
                throw new IllegalStateException("Geofence is null in result but shouldn't be");
            }

            selectedGeofenceId.selectedGeofence(geofence.id());
        }
    }
}
