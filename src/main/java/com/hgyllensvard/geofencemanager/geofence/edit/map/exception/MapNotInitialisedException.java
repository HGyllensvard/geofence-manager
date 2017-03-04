package com.hgyllensvard.geofencemanager.geofence.edit.map.exception;


public class MapNotInitialisedException extends RuntimeException {

    private static final String MAP_NOT_LOADED_ERROR = "The map view has not loaded yet, make sure that initialiseAndDisplayMap has returned successfully before using methods";

    public MapNotInitialisedException() {
        super(MAP_NOT_LOADED_ERROR);
    }
}
