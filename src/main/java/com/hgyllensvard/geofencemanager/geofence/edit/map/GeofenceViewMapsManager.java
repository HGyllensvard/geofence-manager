package com.hgyllensvard.geofencemanager.geofence.edit.map;


import android.support.annotation.Nullable;

import com.hgyllensvard.geofencemanager.geofence.edit.map.geofenceView.GeofenceMapView;

import java.util.HashMap;
import java.util.Map;

public class GeofenceViewMapsManager {

    private Map<Long, GeofenceMapView> geofenceMapManagers;

    public GeofenceViewMapsManager() {
        geofenceMapManagers = new HashMap<>();
    }

    public GeofenceMapView get(long geofenceId) {
        return geofenceMapManagers.get(geofenceId);
    }

    void put(long geofenceId, GeofenceMapView viewMapManager) {
        geofenceMapManagers.put(geofenceId, viewMapManager);
    }

    GeofenceMapView remove(long id) {
        return geofenceMapManagers.remove(id);
    }

    long findGeofenceId(String markerId) {
        GeofenceMapView mapManager = findGeofenceViewMapManager(markerId);

        if (mapManager == null) {
            return -1;
        }

        return mapManager.getGeofence().id();
    }

    @Nullable
    GeofenceMapView findGeofenceViewMapManager(String markerId) {
        for (Map.Entry<Long, GeofenceMapView> markerEntrySet : geofenceMapManagers.entrySet()) {
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
