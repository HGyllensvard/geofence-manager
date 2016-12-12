package com.hgyllensvard.geofencemanager.geofence.persistence;


import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class GeofenceRepository {

    public Flowable<List<GeofenceModel>> listenGeofences() {
        return null;
    }

    public Single<Boolean> save() {
        return null;
    }
}
