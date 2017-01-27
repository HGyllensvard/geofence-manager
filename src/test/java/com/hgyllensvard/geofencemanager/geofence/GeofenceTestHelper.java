package com.hgyllensvard.geofencemanager.geofence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

public class GeofenceTestHelper {
    
    public static final String NAME = "name";
    public static final int LATITUDE = 10;
    public static final int LONGITUDE = 20;
    public static final LatLng LAT_LNG = new LatLng(LATITUDE, LONGITUDE);
    public static final int RADIUS = 30;
    public static final boolean ACTIVE = true;

    public static final Geofence testGeofence = Geofence.create(NAME, LAT_LNG, RADIUS, ACTIVE);
    public static final Geofence insertedTestGeofence = testGeofence.withId(1);

}
