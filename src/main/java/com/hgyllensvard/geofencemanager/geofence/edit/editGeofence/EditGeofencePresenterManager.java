package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EditGeofencePresenterManager {

    private GeofenceManager geofenceManager;
    private SelectedGeofence selectedGeofence;

    @Inject
    public EditGeofencePresenterManager(
            GeofenceManager geofenceManager,
            SelectedGeofence selectedGeofence
    ) {
        this.geofenceManager = geofenceManager;
        this.selectedGeofence = selectedGeofence;
    }

    void updateSelectedGeofence(
            String geofenceName,
            @Nullable LatLng geofencePosition
    ) {
        selectedGeofence.selectedValidGeofence()
                .map(geofence -> constructNewGeofence(geofenceName, geofencePosition, geofence))
                .flatMap(geofence -> geofenceManager.updateGeofence(geofence)
                        .toMaybe())
                .subscribeOn(Schedulers.io())
                .subscribe(actionResult -> Timber.i("Action result: %s", actionResult)
                        , Timber::e);
    }

    private Geofence constructNewGeofence(String geofenceName, @Nullable LatLng geofencePosition, Geofence geofence) {
        Geofence tempGeofence = geofence.withName(geofenceName);

        if (geofencePosition == null) {
            return tempGeofence;
        } else {
            return tempGeofence.withLatLng(geofencePosition);
        }
    }
}
