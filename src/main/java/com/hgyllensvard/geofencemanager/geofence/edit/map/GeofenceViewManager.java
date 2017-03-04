package com.hgyllensvard.geofencemanager.geofence.edit.map;


import android.annotation.SuppressLint;

import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class GeofenceViewManager {

    private final GeofenceManager geofenceManager;
    private final GeofenceMapOptions mapOptions;
    private final SelectedGeofence selectedGeofence;

    @SuppressLint("UseSparseArrays") // Does not support all methods used here
    private final Map<Long, GeofenceView> geofenceViews = new HashMap<>();

    @Inject
    public GeofenceViewManager(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions,
            SelectedGeofence selectedGeofence
    ) {
        this.geofenceManager = geofenceManager;
        this.mapOptions = mapOptions;
        this.selectedGeofence = selectedGeofence;
    }

    public Flowable<GeofenceViewUpdate> observeGeofenceViews() {
        return geofenceManager.observeGeofences()
                .flatMap(this::updateGeofenceViews)
                .subscribeOn(Schedulers.io())
                .share();
    }

    private Flowable<GeofenceViewUpdate> updateGeofenceViews(List<Geofence> updatedGeofences) {
        return Observable.combineLatest(Observable.just(updatedGeofences), selectedGeofence.observeSelectedGeofence(),
                (geofences, selectedGeofenceId) -> {
                    List<GeofenceView> updatedGeofenceViews = new ArrayList<>();
                    List<GeofenceView> removedGeofenceViews = new ArrayList<>();

                    Set<Long> keys = new HashSet<>(geofenceViews.keySet());

                    for (Geofence geofence : geofences) {
                        GeofenceView geofenceView = geofenceViews.get(geofence.id());

                        keys.remove(geofence.id());

                        if (isModifiedGeofence(geofence, geofenceView) && shouldDisplayGeofence(geofence.id(), selectedGeofenceId)) {
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
                }).toFlowable(BackpressureStrategy.BUFFER);
    }

    private boolean shouldDisplayGeofence(long id, long geofenceId) {
        return id == geofenceId;
    }

    private boolean isModifiedGeofence(Geofence geofence, GeofenceView geofenceView) {
        return geofenceView == null || !geofenceView.getGeofence().equals(geofence);
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
