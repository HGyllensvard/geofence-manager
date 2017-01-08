package com.hgyllensvard.geofencemanager.geofence.persistence;

import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.geofence.GeofenceData;
import com.hgyllensvard.geofencemanager.geofence.persistence.exceptions.InsertFailedException;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GeofenceRepository {

    private final GeofenceMapper geofenceMapper;

    private final BriteDatabase database;
    private Flowable<List<GeofenceData>> geofenceFlowable;

    public GeofenceRepository(
            BriteDatabase database,
            GeofenceMapper geofenceMapper
    ) {
        this.database = database;
        this.geofenceMapper = geofenceMapper;

        createGeofenceFlowable();
    }

    public Flowable<List<GeofenceData>> listenGeofences() {
        return geofenceFlowable;
    }

    public Single<GeofenceData> insert(@NonNull final GeofenceData geofence) {
        return Single.fromCallable(() -> {
            long id = database.insert(GeofenceModel.TABLE_NAME, geofenceMapper.toContentValues(geofence));

            if (id != -1) {
                return geofence.withId(id);
            } else {
                throw new InsertFailedException("Couldn't save geofence: " + geofence);
            }
        }).subscribeOn(Schedulers.io());
    }

    public Single<Boolean> delete(GeofenceData geofenceData) {
        return Single.fromCallable(() ->
                database.delete(GeofenceModel.TABLE_NAME, GeofenceDbModel._ID + " = " + geofenceData.id()) != 0)
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> update(GeofenceData geofenceData) {
        return Single.fromCallable(() -> database.update(
                GeofenceModel.TABLE_NAME,
                geofenceMapper.toContentValues(geofenceData),
                GeofenceDbModel._ID + " = " + geofenceData.id()

        )).map(integer -> integer == 1)
                .subscribeOn(Schedulers.io());
    }

    private void createGeofenceFlowable() {
        geofenceFlowable = RxJavaInterop.toV2Flowable(database.createQuery(
                GeofenceModel.TABLE_NAME,
                GeofenceDbModel.SELECT_ALL,
                new String[]{})
                .mapToList(GeofenceModel.SELECT_ALL_MAPPER::map))
                .map(geofenceMapper::toGeofences)
                .subscribeOn(Schedulers.io())
                .share();
    }
}
