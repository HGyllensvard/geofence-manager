package com.hgyllensvard.geofencemanager.geofence.edit.map;


import com.google.auto.value.AutoValue;
import com.hgyllensvard.geofencemanager.geofence.edit.map.geofenceView.GeofenceView;

import java.util.Collections;
import java.util.List;

@AutoValue
public abstract class GeofenceViewUpdate {

    public static final GeofenceViewUpdate EMPTY_UPDATE = GeofenceViewUpdate.create(
            Collections.emptyList(),
            Collections.emptyList(),
            Collections.emptyList());

    public abstract List<GeofenceView> geofenceViews();

    public abstract List<GeofenceView> selectedGeofenceViews();

    public abstract List<GeofenceView> removedGeofenceViews();

    public static GeofenceViewUpdate create(
            List<GeofenceView> geofenceViews,
            List<GeofenceView> selectedGeofenceViews,
            List<GeofenceView> removedGeofenceViews
    ) {
        return new AutoValue_GeofenceViewUpdate(
                geofenceViews,
                selectedGeofenceViews,
                removedGeofenceViews);
    }
}
