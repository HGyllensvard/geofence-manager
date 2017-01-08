package com.hgyllensvard.geofencemanager.geofence.persistence;


import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

import java.util.List;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GeofenceRepository {

    private static final String NAME = "name";

    private final Context context;
    private final GeofenceMapper geofenceMapper;

    private Realm geofenceRealm;
    private Flowable<List<GeofenceData>> geofenceFlowable;

    public GeofenceRepository(
            Context context,
            GeofenceMapper geofenceMapper) {
        this.context = context;
        this.geofenceMapper = geofenceMapper;

        createGeofenceFlowable();
    }

    public Flowable<List<GeofenceData>> listenGeofences() {
        return geofenceFlowable;
    }

    public Single<Boolean> delete(String name) {
        return Single.fromCallable(() -> {
            Realm realm = openRealm();

            boolean result = realm.where(GeofenceModel.class)
                    .equalTo(NAME, name).findAll()
                    .deleteAllFromRealm();

            realm.close();

            return result;
        });
    }

    public Single<Boolean> save(String name, LatLng latLng, int radius) {
        return Single.fromCallable(() -> {
            Realm realm = openRealm();

            GeofenceModel model = geofenceMapper.toModel(name, latLng, radius);

            boolean result = realm.copyToRealm(model) != null;

            realm.close();

            return result;
        }).subscribeOn(Schedulers.io());
    }

    private Realm openRealm() {
        return Realm.getInstance(new RealmConfiguration.Builder(context).build());
    }

    private void createGeofenceFlowable() {
        geofenceFlowable = Flowable.defer(() ->
                RxJavaInterop.toV2Observable(geofenceRealm.where(GeofenceModel.class)
                        .findAll()
                        .asObservable())
                        .map(geofenceMapper::toGeofences)
                        .toFlowable(BackpressureStrategy.BUFFER)
                        .doOnSubscribe(disposable -> geofenceRealm = openRealm())
                        .doOnTerminate(() -> geofenceRealm.close())
                        .share());
    }
}
