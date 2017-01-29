package com.hgyllensvard.geofencemanager.geofence.map;


import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class GeofenceViewUpdate {

    public abstract List<GeofenceView> geofenceViews();

    public abstract List<GeofenceView> updatedGeofenceViews();

    public abstract List<GeofenceView> removedGeofenceViews();

    public static GeofenceViewUpdate create(
            List<GeofenceView> geofenceViews,
            List<GeofenceView> updatedGeofenceViews,
            List<GeofenceView> removedGeofenceViews
    ) {
        return new AutoValue_GeofenceViewUpdate(
                geofenceViews,
                updatedGeofenceViews,
                removedGeofenceViews);
    }
}
