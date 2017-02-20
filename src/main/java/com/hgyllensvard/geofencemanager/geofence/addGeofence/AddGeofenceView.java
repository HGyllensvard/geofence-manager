package com.hgyllensvard.geofencemanager.geofence.addGeofence;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.GeofenceManagerInjector;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;

import javax.inject.Inject;

import io.reactivex.Observable;

public class AddGeofenceView extends View implements AddGeofenceViews {

    // Ugly, not decided how to do this in a good way, factory? Separate module?
    @Inject
    AddSingleGeofencePresenter addSingleGeofencePresenter;

    @Inject
    AddMultipleGeofencePresenter addMultipleGeofencePresenter;

    @Inject
    MapView mapView;

    private AddGeofencePresenter addGeofencePresenter;

    public AddGeofenceView(Context context) {
        super(context);
    }

    public AddGeofenceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        extractFromAttributes(context, attrs);
    }

    public AddGeofenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        extractFromAttributes(context, attrs);
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
    public Observable<LatLng> observerLongClick() {
        return mapView.observerLongClick();
    }

    private void extractFromAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = getTypedArray(context, attrs);

        extractPresenterType(typedArray);
    }

    private TypedArray getTypedArray(Context context, AttributeSet attrs) {
        return context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AddGeofenceView,
                0, 0);
    }

    private void extractPresenterType(TypedArray typedArray) {
        try {
//            if (!typedArray.hasValue(R.styleable.AddGeofenceView_presenterType)) {
//                throw new IllegalArgumentException("Value for the presenter type not set");
//            }

            int presenter = typedArray.getInt(R.styleable.AddGeofenceView_presenterType, 1);

            switch (presenter) {
                case 0:
                    addGeofencePresenter = addSingleGeofencePresenter;
                    break;
                case 1:
                    addGeofencePresenter = addMultipleGeofencePresenter;
                    break;
            }
        } finally {
            typedArray.recycle();
        }
    }
}
