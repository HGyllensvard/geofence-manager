package com.hgyllensvard.geofencemanager.geofence.edit.map.geofenceView;


import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.edit.map.util.MapCalculationsUtils;

@PerActivity
public class GeofenceMapViewFactory {

    private MapCalculationsUtils mapCalculationsUtils;
    private GeofenceMapOptions geofenceMapOptions;

    public GeofenceMapViewFactory(
            MapCalculationsUtils mapCalculationsUtils,
            GeofenceMapOptions geofenceMapOptions
    ) {
        this.mapCalculationsUtils = mapCalculationsUtils;
        this.geofenceMapOptions = geofenceMapOptions;
    }

    public GeofenceMapView createGeofenceMapView(GeofenceView geofenceView) {
        return new GeofenceMapView(geofenceView, geofenceMapOptions, mapCalculationsUtils);
    }
}
