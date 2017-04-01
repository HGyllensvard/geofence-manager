package com.hgyllensvard.geofencemanager.geofence.helpers;


import com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceMapOptions;

public class GeofenceMapOptionsTestHelper {

    public static final int FILL_COLOUR = 1;

    public static final int STROKE_COLOUR = 2;

    public static final GeofenceMapOptions GEOFENCE_MAP_OPTIONS = GeofenceMapOptions.create()
            .fillColor(1)
            .strokeColor(2)
            .geofenceCreatedName(GeofenceTestHelper.NAME_ONE)
            .geofenceCreatedRadius(GeofenceTestHelper.RADIUS_ONE)
            .build();
}
