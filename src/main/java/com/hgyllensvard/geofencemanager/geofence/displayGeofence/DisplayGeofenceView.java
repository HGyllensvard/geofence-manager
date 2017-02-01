package com.hgyllensvard.geofencemanager.geofence.displayGeofence;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.hgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceView;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class DisplayGeofenceView extends FrameLayout implements DisplayGeofenceViews {

    @Inject
    DisplayGeofencePresenter displayGeofencePresenter;

    @Inject
    MapView mapView;

    public DisplayGeofenceView(Context context) {
        super(context);
    }

    public DisplayGeofenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayGeofenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        injectDependencies();

        inflate(getContext(), R.layout.display_geofence_view, this);

        displayGeofencePresenter.bindView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        displayGeofencePresenter.unbindView();
    }

    private void injectDependencies() {
        if (getContext() instanceof AppCompatActivity) {
            ((GeofenceManagerActivity) getContext()).getGeofenceComponent()
                    .inject(this);
        } else {
            throw new IllegalStateException("Activity not build to support this view");
        }
    }

    @Override
    public Observable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public void displayGeofenceViews(List<GeofenceView> geofenceViews) {
        mapView.updateGeofenceViews(geofenceViews);
    }

    @Override
    public void removeGeofenceViews(List<GeofenceView> geofenceViews) {
        mapView.removedGeofenceViews(geofenceViews);
    }

    @Override
    public void animateCameraTo(CameraUpdate cameraUpdate) {
        mapView.animateCameraTo(cameraUpdate);
    }

    @Override
    public void destroy() {

    }
}
