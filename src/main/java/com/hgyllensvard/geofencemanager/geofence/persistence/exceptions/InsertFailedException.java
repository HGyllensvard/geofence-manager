package com.hgyllensvard.geofencemanager.geofence.persistence.exceptions;

public class InsertFailedException extends RuntimeException {

    public InsertFailedException(String message) {
        super(message);
    }
}
