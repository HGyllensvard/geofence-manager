package com.hgyllensvard.geofencemanager.geofence.geofence;

public class FailedToUpdateGeofenceException extends Throwable {

    public FailedToUpdateGeofenceException(Geofence geofence) {
        super("Could not update Geofence:" + geofence);
    }
}
