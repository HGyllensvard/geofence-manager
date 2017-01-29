package com.hgyllensvard.geofencemanager.geofence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import java.util.Arrays;
import java.util.List;

public class GeofenceTestHelper {

    public static final String NAME_ONE = "name_one";
    public static final int LATITUDE_ONE = 10;
    public static final int LONGITUDE_ONE = 20;
    public static final LatLng LAT_LNG_ONE = new LatLng(LATITUDE_ONE, LONGITUDE_ONE);
    public static final int RADIUS_ONE = 30;
    public static final boolean IS_ACTIVE_ONE = true;

    public static final Geofence TEST_GEOFENCE_ONE = Geofence.create(NAME_ONE, LAT_LNG_ONE, RADIUS_ONE, IS_ACTIVE_ONE);
    public static final Geofence TEST_GEOFENCE_ONE_WITH_ID = TEST_GEOFENCE_ONE.withId(1);

    public static final String NAME_TWO = "name_two";
    public static final int LATITUDE_TWO = 15;
    public static final int LONGITUDE_TWO = 25;
    public static final LatLng LAT_LNG_TWO = new LatLng(LATITUDE_TWO, LONGITUDE_TWO);
    public static final int RADIUS_TWO = 35;
    public static final boolean IS_ACTIVE_TWO = false;

    public static final Geofence TEST_GEOFENCE_TWO = Geofence.create(NAME_TWO, LAT_LNG_TWO, RADIUS_TWO, IS_ACTIVE_TWO);
    public static final Geofence TEST_GEOFENCE_TWO_WITH_ID = TEST_GEOFENCE_TWO.withId(2);

    public static final List<Geofence> TEST_GEOFENCES = Arrays.asList(TEST_GEOFENCE_ONE, TEST_GEOFENCE_TWO);
    public static final List<Geofence> TEST_GEOFENCES_WITH_ID = Arrays.asList(TEST_GEOFENCE_ONE_WITH_ID, TEST_GEOFENCE_TWO_WITH_ID);
}
