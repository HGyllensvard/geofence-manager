package com.hgyllensvard.geofencemanager.geofence.edit.map;


import android.annotation.SuppressLint;

import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceIdState;

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
import timber.log.Timber;

public class GeofenceViewManager {

    private final GeofenceManager geofenceManager;
    private final GeofenceMapOptions mapOptions;
    private final SelectedGeofenceId selectedGeofenceId;

    @SuppressLint("UseSparseArrays") // Does not support all methods used here
    private final Map<Long, GeofenceView> geofenceViews = new HashMap<>();

    @Inject
    public GeofenceViewManager(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions,
            SelectedGeofenceId selectedGeofenceId
    ) {
        this.geofenceManager = geofenceManager;
        this.mapOptions = mapOptions;
        this.selectedGeofenceId = selectedGeofenceId;
    }

    public Flowable<GeofenceViewUpdate> observeGeofenceViews() {
        return geofenceManager.observeGeofences()
                .flatMap(this::updateGeofenceViews)
                .subscribeOn(Schedulers.io())
                .share();
    }

    private Flowable<GeofenceViewUpdate> updateGeofenceViews(List<Geofence> updatedGeofences) {
        return Observable.combineLatest(selectedGeofenceId.observeValidSelectedGeofenceId(), Observable.just(updatedGeofences),
                (selectedGeofenceState, geofences) -> {
                    List<GeofenceView> updatedGeofenceViews = new ArrayList<>();
                    List<GeofenceView> removedGeofenceViews = new ArrayList<>();

                    Set<Long> keys = new HashSet<>(geofenceViews.keySet());

                    for (Geofence geofence : geofences) {
                        GeofenceView geofenceView = geofenceViews.get(geofence.id());

                        keys.remove(geofence.id());

                        if (isModifiedGeofence(geofence, geofenceView) && shouldDisplayGeofence(geofence.id(), selectedGeofenceState)) {
                            Timber.v("Creating GeofenceView for: %s", geofence);
                            GeofenceView view = createGeofenceView(geofence);
                            geofenceViews.put(geofence.id(), view);
                            updatedGeofenceViews.add(view);
                        }
                    }

                    // We didn't parse the key, the geofence is to be removed.
                    for (long key : keys) {
                        Timber.v("Removing GeofenceView for id: %s", key);
                        removedGeofenceViews.add(geofenceViews.remove(key));
                    }

                    return GeofenceViewUpdate.create(
                            new ArrayList<>(geofenceViews.values()),
                            updatedGeofenceViews,
                            removedGeofenceViews
                    );
                }).toFlowable(BackpressureStrategy.BUFFER);
    }

    private boolean shouldDisplayGeofence(long id, SelectedGeofenceIdState geofenceState) {
        return id == geofenceState.geofenceId();
    }

    private boolean isModifiedGeofence(Geofence geofence, GeofenceView geofenceView) {
        return geofenceView == null || !geofenceView.geofence().equals(geofence);
    }

    private GeofenceView createGeofenceView(Geofence geofence) {
        return GeofenceView.create(geofence,
                mapOptions.fillColor(),
                mapOptions.strokeColor());
    }
}
