package com.hgyllensvard.geofencemanager.geofence.addGeofence;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMapOptions;

import io.reactivex.Observable;
import timber.log.Timber;

public class AddSingleGeofencePresenter extends AddGeofencePresenter {

    private long geofenceId;

    public AddSingleGeofencePresenter(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions
    ) {
        super(geofenceManager, mapOptions);

        geofenceId = Geofence.NO_ID;
    }

    @NonNull
    @Override
    Observable<Boolean> shouldAddNewGeofence(LatLng latLng) {
        return Observable.defer(() -> {
            if (geofenceId == Geofence.NO_ID) {
                return Observable.just(true);
            } else {
                return filterIfGeofenceExist();
            }
        });
    }

    /**
     * If the Geofence exist in the database it means the geofence that was
     * added before still exist, so filter that observable so another one isn't created;
     */
    private Observable<Boolean> filterIfGeofenceExist() {
        return geofenceManager.getGeofence(geofenceId)
                .onErrorReturn(throwable -> Geofence.sDummyGeofence)
                .doOnSuccess(geofence -> Timber.v("Fetched geofence: %s", geofence))
                .doOnSuccess(geofence -> {
                    if (geofence.equals(Geofence.sDummyGeofence)) {
                        geofenceId = Geofence.NO_ID;
                    }
                })
                .filter(geofence -> !geofence.equals(Geofence.sDummyGeofence))
                .map(geofence -> true)
                .toObservable();
    }

    @Override
    void geofenceAdded(Geofence geofence) {
        geofenceId = geofence.id();
    }
}
