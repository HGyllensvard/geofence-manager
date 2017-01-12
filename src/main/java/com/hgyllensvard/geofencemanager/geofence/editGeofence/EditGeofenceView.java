package com.hgyllensvard.geofencemanager.geofence.editGeofence;


import android.app.Activity;
import android.view.View;

import com.google.android.gms.maps.model.Marker;
import com.hgyllensvard.geofencemanager.R2;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;

public class EditGeofenceView implements EditGeofenceViews {

    @BindView(R2.id.geofence_map_selected_geofence)
    View editSelectedGeofenceOptions;

    private final MapView mapView;

    private Unbinder unbinder;

    public EditGeofenceView(
            Activity activity,
            MapView mapView
    ) {
        this.mapView = mapView;

        unbinder = ButterKnife.bind(this, activity);
    }

    @Override
    public Flowable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public Flowable<Marker> observeMarkerSelected() {
        return mapView.observeMarkerSelected();
    }

    @Override
    public Flowable<Integer> observeCameraStartedMoving() {
        return mapView.observeCameraStartMoving();
    }

    @Override
    public void hideSelectedGeofenceOptions() {
        editSelectedGeofenceOptions.setVisibility(View.GONE);
    }

    @Override
    public void displaySelectedGeofenceOptions() {
        editSelectedGeofenceOptions.setVisibility(View.VISIBLE);
    }

    @Override
    public void destroy() {
        unbinder.unbind();
    }
}
