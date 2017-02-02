package com.hgyllensvard.geofencemanager.geofence.addGeofence;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import javax.inject.Inject;

import io.reactivex.Observable;

public class AddGeofenceView extends FrameLayout implements AddGeofenceViews {

    @Inject
    AddGeofencePresenter addGeofencePresenter;

    @Inject
    MapView mapView;

    public AddGeofenceView(Context context) {
        super(context);
    }

    public AddGeofenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AddGeofenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        injectDependencies();

        inflate(getContext(), R.layout.add_geofence_view, this);

        addGeofencePresenter.bindView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        addGeofencePresenter.unbindView();
    }

    private void injectDependencies() {
        if (getContext() instanceof GeofenceManagerActivity) {
            ((GeofenceManagerActivity) getContext()).getGeofenceManagerActivityComponent()
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
    public Observable<LatLng> observerLongClick() {
        return mapView.observerLongClick();
    }

    @Override
    public void destroy() {

    }
}
