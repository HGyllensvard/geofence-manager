package com.hgyllensvard.geofencemanager.geofence.geofence;

class FailedToUpdateGeofenceException extends Throwable {

    FailedToUpdateGeofenceException(Geofence geofence) {
        super("Could not update Geofence:" + geofence);
    }
}
