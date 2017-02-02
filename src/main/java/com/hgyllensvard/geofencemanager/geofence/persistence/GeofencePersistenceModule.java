package com.hgyllensvard.geofencemanager.geofence.persistence;


import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rx.schedulers.Schedulers;

@Module
public class GeofencePersistenceModule {

    private static final int VERSION = 1;

    @Singleton
    @Provides
    public BriteDatabase providesDatabase(SQLiteOpenHelper helper) {
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        return sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    }

    @Singleton
    @Provides
    GeofenceRepository providesGeofenceRepository(
            BriteDatabase briteDatabase,
            GeofenceMapper geofenceMapper
    ) {
        return new GeofenceRepository(briteDatabase, geofenceMapper);
    }

    @Singleton
    @Provides
    SQLiteOpenHelper providesSqlLiteOpenHelper(
            Context context
    ) {
        return new GeofenceSqlOpenHelper(context, VERSION);
    }

    @Singleton
    @Provides
    GeofenceMapper providesGeofenceMapper() {
        return new GeofenceMapper();
    }
}
