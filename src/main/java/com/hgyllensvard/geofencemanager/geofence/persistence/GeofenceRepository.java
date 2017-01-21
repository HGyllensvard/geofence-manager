package com.hgyllensvard.geofencemanager.geofence.persistence;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.persistence.exceptions.InsertFailedException;
import com.hgyllensvard.geofencemanager.geofence.persistence.exceptions.NoSuchGeofenceExistException;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class GeofenceRepository {

    private final GeofenceMapper geofenceMapper;

    private final BriteDatabase database;
    private Flowable<List<Geofence>> geofenceFlowable;

    public GeofenceRepository(
            BriteDatabase database,
            GeofenceMapper geofenceMapper
    ) {
        this.database = database;
        this.geofenceMapper = geofenceMapper;

        createGeofenceFlowable();
    }

    public Flowable<List<Geofence>> listenGeofences() {
        return geofenceFlowable;
    }

    public Single<Geofence> insert(@NonNull final Geofence geofence) {
        return Single.fromCallable(() -> {
            long id = database.insert(GeofenceModel.TABLE_NAME, geofenceMapper.toContentValues(geofence));

            if (id != -1) {
                return geofence.withId(id);
            } else {
                throw new InsertFailedException("Couldn't save geofence: " + geofence);
            }
        }).subscribeOn(Schedulers.io());
    }

    public Single<Boolean> delete(long geofenceId) {
        return Single.fromCallable(() ->
                database.delete(GeofenceModel.TABLE_NAME, GeofenceDbModel._ID + " = " + geofenceId) != 0)
                .subscribeOn(Schedulers.io());
    }

    public Single<Boolean> update(Geofence geofence) {
        return Single.fromCallable(() -> database.update(
                GeofenceModel.TABLE_NAME,
                geofenceMapper.toContentValues(geofence),
                GeofenceDbModel._ID + " = " + geofence.id()

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

    public Single<Geofence> getGeofence(long identifier) {
        return Single.fromCallable(() -> {

            Cursor cursor = database.query(GeofenceDbModel.SELECT_WITH_ID, String.valueOf(identifier));

            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        final GeofenceModel eventModel = GeofenceModel.SELECT_ALL_MAPPER.map(cursor);
                        return geofenceMapper.toGeofence(eventModel);
                    }
                } finally {
                    cursor.close();
                }
            }

            throw new NoSuchGeofenceExistException("No geofence found for ID: " + identifier);
        });
    }
}
