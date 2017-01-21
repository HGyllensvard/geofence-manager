package com.hgyllensvard.geofencemanager.geofence.map;


import android.annotation.SuppressLint;

import com.hgyllensvard.geofencemanager.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.GeofenceMapOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class GeofenceViewManager {

    private final GeofenceMapOptions mapOptions;

    @SuppressLint("UseSparseArrays") // Does not support all methods used here
    private final Map<Long, GeofenceView> geofenceViews = new HashMap<>();
    private final BehaviorSubject<List<GeofenceView>> geofenceViewSubject;
    private final PublishSubject<List<GeofenceView>> updatedGeofenceViewSubject;
    private final PublishSubject<List<GeofenceView>> removedGeofenceViewSubject;

    public GeofenceViewManager(
            GeofenceMapOptions mapOptions
    ) {
        this.mapOptions = mapOptions;

        geofenceViewSubject = BehaviorSubject.create();
        updatedGeofenceViewSubject = PublishSubject.create();
        removedGeofenceViewSubject = PublishSubject.create();
    }

    void updateGeofenceViews(List<Geofence> geofences) {
        List<GeofenceView> updatedGeofenceViews = new ArrayList<>();
        List<GeofenceView> removedGeofenceViews = new ArrayList<>();

        Set<Long> keys = geofenceViews.keySet();

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

        geofenceViewSubject.onNext(new ArrayList<>(geofenceViews.values()));
        updatedGeofenceViewSubject.onNext(updatedGeofenceViews);
        removedGeofenceViewSubject.onNext(removedGeofenceViews);
    }

    Observable<List<GeofenceView>> observeGeofenceViews() {
        return geofenceViewSubject;
    }

    Observable<List<GeofenceView>> observeUpdatedGeofenceViews() {
        return updatedGeofenceViewSubject;
    }

    Observable<List<GeofenceView>> observeRemovedGeofenceViews() {
        return removedGeofenceViewSubject;
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
