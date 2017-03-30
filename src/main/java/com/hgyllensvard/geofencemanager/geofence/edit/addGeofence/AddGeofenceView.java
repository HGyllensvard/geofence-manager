package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.GeofenceManagerInjector;
import com.hgyllensvard.geofencemanager.geofence.edit.map.MapView;

import javax.inject.Inject;

import io.reactivex.Observable;

public class AddGeofenceView extends View implements AddGeofenceViews {

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

        if (!isInEditMode()) {
            injectDependencies();

            addGeofencePresenter.bindView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        addGeofencePresenter.unbindView();
    }

    private void injectDependencies() {
        if (getContext() instanceof GeofenceManagerInjector) {
            ((GeofenceManagerInjector) getContext()).getGeofenceManagerComponent()
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
    public Observable<LatLng> observerLongClick() {
        return mapView.observeLongClick();
    }
}
