package com.hgyllensvard.geofencemanager.geofence.helpers;


import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceActionResult;

import io.reactivex.Single;

public class GeofenceActionResultTestHelper {

    public static final GeofenceActionResult FAILED_GEOFENCE_ACTION = GeofenceActionResult.failure(new RuntimeException("TestFailure"));

    public static final Single<GeofenceActionResult> SUCCESS_SINGLE_GEOFENCE_ONE = Single.just(GeofenceActionResult.success(GeofenceTestHelper.GEOFENCE_ONE));
}
