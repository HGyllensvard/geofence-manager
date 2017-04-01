package com.hgyllensvard.geofencemanager.geofence.helpers;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceResult;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceState;

import java.util.Arrays;
import java.util.List;

public class GeofenceTestHelper {

    public static final long ID_ONE = 1;
    public static final String ID_ONE_STR = String.valueOf(ID_ONE);
    public static final String NAME_ONE = "name_one";
    public static final int LATITUDE_ONE = 10;
    public static final int LONGITUDE_ONE = 20;
    public static final LatLng LAT_LNG_ONE = new LatLng(LATITUDE_ONE, LONGITUDE_ONE);
    public static final int RADIUS_ONE = 30;
    public static final boolean IS_ACTIVE_ONE = true;

    public static final Geofence GEOFENCE_ONE_NO_ID = Geofence.create(NAME_ONE, LAT_LNG_ONE, RADIUS_ONE, IS_ACTIVE_ONE);
    public static final Geofence GEOFENCE_ONE = GEOFENCE_ONE_NO_ID.withId(ID_ONE);

    public static final GeofenceResult GEOFENCE_ONE_RESULT = GeofenceResult.success(GEOFENCE_ONE);

    public static final SelectedGeofenceState GEOFENCE_ONE_STATE_NO_ID = SelectedGeofenceState.selectedGeofence(GEOFENCE_ONE_NO_ID);
    public static final SelectedGeofenceState GEOFENCE_ONE_STATE = SelectedGeofenceState.selectedGeofence(GEOFENCE_ONE);

    public static final long ID_TWO = 2;
    public static final String ID_TWO_STR = String.valueOf(ID_TWO);
    public static final String NAME_TWO = "name_two";
    public static final int LATITUDE_TWO = 15;
    public static final int LONGITUDE_TWO = 25;
    public static final LatLng LAT_LNG_TWO = new LatLng(LATITUDE_TWO, LONGITUDE_TWO);
    public static final int RADIUS_TWO = 35;
    public static final boolean IS_ACTIVE_TWO = false;

    public static final Geofence GEOFENCE_TWO_NO_ID = Geofence.create(NAME_TWO, LAT_LNG_TWO, RADIUS_TWO, IS_ACTIVE_TWO);
    public static final Geofence GEOFENCE_TWO = GEOFENCE_TWO_NO_ID.withId(ID_TWO);

    public static final GeofenceResult GEOFENCE_TWO_RESULT = GeofenceResult.success(GEOFENCE_TWO);

    public static final SelectedGeofenceState GEOFENCE_TWO_STATE_NO_ID = SelectedGeofenceState.selectedGeofence(GEOFENCE_TWO_NO_ID);
    public static final SelectedGeofenceState GEOFENCE_TWO_STATE = SelectedGeofenceState.selectedGeofence(GEOFENCE_TWO);

    public static final List<Geofence> TEST_GEOFENCES = Arrays.asList(GEOFENCE_ONE_NO_ID, GEOFENCE_TWO_NO_ID);
    public static final List<Geofence> TEST_GEOFENCES_WITH_ID = Arrays.asList(GEOFENCE_ONE, GEOFENCE_TWO);
}
