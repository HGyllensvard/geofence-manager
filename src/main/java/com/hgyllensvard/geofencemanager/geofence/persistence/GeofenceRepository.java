package com.hgyllensvard.geofencemanager.geofence.persistence;


import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GeofenceRepository {

    private Realm realm;
    private Context context;

    public GeofenceRepository(
            Context context
    ) {
        this.context = context;
    }

    public Flowable<List<GeofenceModel>> listenGeofences() {
        return null;
    }

    public Single<Boolean> delete(String name) {
        return null;
    }

    public Single<Boolean> save(String name, LatLng latLng) {
        return Single.fromCallable(() -> {
            Realm realm = openRealm();

            GeofenceModel geofenceModel = new GeofenceModel();
            geofenceModel.name = name;
            geofenceModel.latitude = latLng.latitude;
            geofenceModel.longitude = latLng.longitude;
            realm.copyToRealm(geofenceModel);

            boolean result = realm.copyToRealm(geofenceModel) != null;

            realm.close();

            return result;
        }).subscribeOn(Schedulers.io());
    }

    private Realm openRealm() {
        return Realm.getInstance(new RealmConfiguration.Builder(context).build());
    }
}
