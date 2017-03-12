package com.hgyllensvard.geofencemanager.geofence.edit.map;


import java.util.HashMap;
import java.util.Map;

public class GeofenceViewMapsManager {

    private Map<Long, GeofenceViewMapManager> geofenceMapManagers;

    public GeofenceViewMapsManager() {
        geofenceMapManagers = new HashMap<>();
    }

    public GeofenceViewMapManager get(long geofenceId) {
        return geofenceMapManagers.get(geofenceId);
    }

    void put(long geofenceId, GeofenceViewMapManager viewMapManager) {
        geofenceMapManagers.put(geofenceId, viewMapManager);
    }

    long findGeofenceId(String markerId) {
        for (Map.Entry<Long, GeofenceViewMapManager> markerEntrySet : geofenceMapManagers.entrySet()) {
            if (markerEntrySet.getValue().isMarker(markerId)) {
                return markerEntrySet.getKey();
            }
        }

        return -1;
    }
}
