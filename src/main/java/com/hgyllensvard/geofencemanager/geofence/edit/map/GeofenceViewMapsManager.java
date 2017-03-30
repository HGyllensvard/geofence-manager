package com.hgyllensvard.geofencemanager.geofence.edit.map;


import android.support.annotation.Nullable;

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

    GeofenceViewMapManager remove(long id) {
        return geofenceMapManagers.remove(id);
    }

    long findGeofenceId(String markerId) {
        GeofenceViewMapManager mapManager = findGeofenceViewMapManager(markerId);

        if (mapManager == null) {
            return -1;
        }

        return mapManager.getGeofence().id();
    }

    @Nullable
    GeofenceViewMapManager findGeofenceViewMapManager(String markerId) {
        for (Map.Entry<Long, GeofenceViewMapManager> markerEntrySet : geofenceMapManagers.entrySet()) {
            if (markerEntrySet.getValue().isMarker(markerId)) {
                return markerEntrySet.getValue();
            }
        }

        return null;
    }

    public void clear() {
        geofenceMapManagers.clear();
    }
}
