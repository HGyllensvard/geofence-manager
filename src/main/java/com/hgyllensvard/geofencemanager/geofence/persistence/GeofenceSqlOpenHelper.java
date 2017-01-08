package com.hgyllensvard.geofencemanager.geofence.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GeofenceSqlOpenHelper extends SQLiteOpenHelper {

    private static final String NAME = "geofence.db";

    public GeofenceSqlOpenHelper(Context context, int version) {
        super(context, NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GeofenceModel.CREATE_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
