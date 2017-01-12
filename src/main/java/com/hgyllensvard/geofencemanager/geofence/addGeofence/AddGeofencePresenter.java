package com.hgyllensvard.geofencemanager.geofence.addGeofence;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.GeofenceMapOptions;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddGeofencePresenter extends PresenterAdapter<AddGeofenceViews> {

    private static final int LONG_CLICK_DEBOUNCE_TIMER = 1;
    private static final TimeUnit LONG_CLICK_DEBOUNCE_TIMER_UNIT = TimeUnit.SECONDS;

    private final GeofenceManager geofenceManager;
    private final GeofenceMapOptions mapOptions;

    private final CompositeDisposable disposableContainer;

    public AddGeofencePresenter(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions
    ) {
        this.geofenceManager = geofenceManager;
        this.mapOptions = mapOptions;

        disposableContainer = new CompositeDisposable();
    }

    @Override
    public void bindView(@NonNull AddGeofenceViews view) {
        super.bindView(view);

        view.displayMap()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> subscribeLongClick(),
                        Timber::e);
    }

    private void subscribeLongClick() {
        Disposable disposable = view.observerLongClick()
                .observeOn(Schedulers.io())
                .debounce(LONG_CLICK_DEBOUNCE_TIMER, LONG_CLICK_DEBOUNCE_TIMER_UNIT)
                .doOnNext(latLng -> Timber.v("Adding new geofence at: %s", latLng))
                .subscribe(this::addGeofence,
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private void addGeofence(LatLng latLng) {
        disposableContainer.add(
                geofenceManager.addGeofence(createNewGeofence(latLng))
                        .subscribeOn(Schedulers.io())
                        .subscribe(geofence -> Timber.i("Geofence added: %s", geofence),
                                Timber::e));
    }

    private Geofence createNewGeofence(LatLng latLng) {
        return Geofence.create(mapOptions.geofenceCreatedName(), latLng, mapOptions.geofenceCreatedRadius());
    }

    @Override
    public void unbindView() {
        super.unbindView();

        disposableContainer.clear();
    }
}
