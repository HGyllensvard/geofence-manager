package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;


class GeofenceAlreadySelectedError extends Throwable {

    public GeofenceAlreadySelectedError(long selectedGeofence) {
        super("Geofence with id already selected: " + selectedGeofence);
    }
}
