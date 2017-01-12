package com.hgyllensvard.geofencemanager.geofence.map;


import android.annotation.SuppressLint;

import com.hgyllensvard.geofencemanager.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.GeofenceMapOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeofenceMarkerManager {

    @SuppressLint("UseSparseArrays") // Does not support all methods used here
    private final Map<Long, GeofenceMarker> markers = new HashMap<>();

    private final GeofenceMapOptions mapOptions;

    public GeofenceMarkerManager(
            GeofenceMapOptions mapOptions
    ) {
        this.mapOptions = mapOptions;
    }

    public List<GeofenceMarker> createMarkers(List<Geofence> geofences) {
        for (Geofence geofence : geofences) {
            GeofenceMarker marker = markers.get(geofence.id());

            if (marker == null) {
                createAndAddMarker(geofence);
            } else {
                if (!geofence.equals(marker.getGeofence())) {
                    createAndAddMarker(geofence);
                }
            }
        }

        return new ArrayList<>(markers.values());
    }

    private void createAndAddMarker(Geofence geofence) {
        GeofenceMarker tempMarker = new GeofenceMarker(geofence,
                mapOptions.fillColor(),
                mapOptions.strokeColor());

        markers.put(geofence.id(), tempMarker);
    }

    public long findGeofenceId(String markerId) {
        for (Map.Entry<Long, GeofenceMarker> markerEntrySet : markers.entrySet()) {
            if (markerEntrySet.getValue().isMarker(markerId)) {
                return markerEntrySet.getKey();
            }
        }

        return -1;
    }
}
