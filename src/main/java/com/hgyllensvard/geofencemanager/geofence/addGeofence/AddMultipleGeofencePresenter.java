package com.hgyllensvard.geofencemanager.geofence.addGeofence;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMapOptions;

import io.reactivex.Observable;

public class AddMultipleGeofencePresenter extends AddGeofencePresenter {

    public AddMultipleGeofencePresenter(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions
    ) {
        super(geofenceManager, mapOptions);
    }

    @NonNull
    @Override
    Observable<Boolean> shouldAddNewGeofence(LatLng latLng) {
        return Observable.just(true);
    }

    @Override
    void geofenceAdded(Geofence geofence) {

    }
}
