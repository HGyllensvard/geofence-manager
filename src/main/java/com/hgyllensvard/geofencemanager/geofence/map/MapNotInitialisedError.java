package com.hgyllensvard.geofencemanager.geofence.map;


public class MapNotInitialisedError extends RuntimeException {

    private static final String MAP_NOT_LOADED_ERROR = "The map view has not loaded yet, make sure that initialiseAndDisplayMap has returned successfully before using methods";

    public MapNotInitialisedError() {
        super(MAP_NOT_LOADED_ERROR);
    }
}
