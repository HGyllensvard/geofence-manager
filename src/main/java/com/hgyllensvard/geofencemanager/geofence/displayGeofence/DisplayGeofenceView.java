package com.hgyllensvard.geofencemanager.geofence.displayGeofence;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.hgyllensvard.geofencemanager.GeofenceManagerInjector;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceView;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import timber.log.Timber;

public class DisplayGeofenceView extends View implements DisplayGeofenceViews {

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

        if (!isInEditMode()) {
            injectDependencies();

            displayGeofencePresenter.bindView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        displayGeofencePresenter.unbindView();
    }

    private void injectDependencies() {
        Timber.wtf("Context: %s", getContext());
        if (getContext() instanceof GeofenceManagerInjector) {
            ((GeofenceManagerInjector) getContext()).getGeofenceManagerActivityComponent()
                    .inject(this);
        } else {
            throw new IllegalStateException("Context does not implement: %s" + GeofenceManagerInjector.class.getSimpleName());
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
}
