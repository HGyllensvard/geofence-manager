package com.hgyllensvard.geofencemanager.geofence.playIntegration;


import java.util.List;

public class GeofenceEventTriggeredManager {

    public GeofenceEventTriggeredManager() {

    }

//    public Observable<Geofence> observeEnteredGeofence() {
//        return null;
//    }
//
//    public Observable<Geofence> observeGeofence() {
//        return null;
//    }
//
//    public Observable<Geofence> observeEnteredGeofence() {
//        return null;
//    }

    void triggerDwelling(List<com.google.android.gms.location.Geofence> triggeredGeofences) {
        for (com.google.android.gms.location.Geofence triggeredGeofence : triggeredGeofences) {
            triggeredGeofence.getRequestId();
        }
    }

    void triggerEntered(List<com.google.android.gms.location.Geofence> triggeredGeofences) {

    }

    void triggerLeft(List<com.google.android.gms.location.Geofence> triggeredGeofences) {

    }
}
