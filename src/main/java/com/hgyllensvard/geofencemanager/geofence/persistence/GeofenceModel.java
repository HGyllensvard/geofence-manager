package com.hgyllensvard.geofencemanager.geofence.persistence;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GeofenceModel extends RealmObject {

    @PrimaryKey
    public String name;

    public double latitude;

    public double longitude;

    public int radius;
}
