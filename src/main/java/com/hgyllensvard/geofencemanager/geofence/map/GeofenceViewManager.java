package com.hgyllensvard.geofencemanager.geofence.map;


import android.annotation.SuppressLint;

import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class GeofenceViewManager {

    private final GeofenceManager geofenceManager;
    private final GeofenceMapOptions mapOptions;

    @SuppressLint("UseSparseArrays") // Does not support all methods used here
    private final Map<Long, GeofenceView> geofenceViews = new HashMap<>();

    public GeofenceViewManager(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions
    ) {
        this.geofenceManager = geofenceManager;
        this.mapOptions = mapOptions;
    }

    public Flowable<GeofenceViewUpdate> observeGeofenceViews() {
        return geofenceManager.observeGeofences()
                .map(this::updateGeofenceViews)
                .subscribeOn(Schedulers.io())
                .share();
    }

    private GeofenceViewUpdate updateGeofenceViews(List<Geofence> geofences) {
        List<GeofenceView> updatedGeofenceViews = new ArrayList<>();
        List<GeofenceView> removedGeofenceViews = new ArrayList<>();

        Set<Long> keys = new HashSet<>(geofenceViews.keySet());

        for (Geofence geofence : geofences) {
            GeofenceView geofenceView = geofenceViews.get(geofence.id());

            keys.remove(geofence.id());

            if (geofenceView == null || !geofenceView.getGeofence().equals(geofence)) {
                GeofenceView view = createGeofenceView(geofence);
                geofenceViews.put(geofence.id(), view);
                updatedGeofenceViews.add(view);
            }
        }

        // We didn't parse the key, the geofence is to be removed.
        for (Long key : keys) {
            removedGeofenceViews.add(geofenceViews.remove(key));
        }

        return GeofenceViewUpdate.create(
                new ArrayList<>(geofenceViews.values()),
                updatedGeofenceViews,
                removedGeofenceViews
        );
    }

    long findGeofenceId(String markerId) {
        for (Map.Entry<Long, GeofenceView> markerEntrySet : geofenceViews.entrySet()) {
            if (markerEntrySet.getValue().isMarker(markerId)) {
                return markerEntrySet.getKey();
            }
        }

        return -1;
    }

    private GeofenceView createGeofenceView(Geofence geofence) {
        return new GeofenceView(geofence,
                mapOptions.fillColor(),
                mapOptions.strokeColor());
    }
}
