package com.henrikgyllensvard.geofencemanager.geofence;


import com.henrikgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.henrikgyllensvard.geofencemanager.geofence.view.GeofenceManagerView;

public class GeofenceManagerPresenter extends PresenterAdapter<GeofenceManagerView> {

    public GeofenceManagerPresenter(
            GeofenceManagerView viewActions
    ) {
        super(viewActions);
    }
}
