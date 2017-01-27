package com.hgyllensvard.geofencemanager.geofence.addGeofence;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMapOptions;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddGeofencePresenter extends PresenterAdapter<AddGeofenceViews> {

    private final GeofenceManager geofenceManager;
    private final GeofenceMapOptions mapOptions;

    final CompositeDisposable disposableContainer;

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

        Disposable disposable = view.displayMap()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> subscribeLongClick(),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private void subscribeLongClick() {
        Disposable disposable = view.observerLongClick()
                .observeOn(Schedulers.io())
                .doOnNext(latLng -> Timber.v("Adding new geofence at: %s", latLng))
                .doOnError(Timber::e)
                .retry()
                .subscribe(this::addGeofence);

        disposableContainer.add(disposable);
    }

    private void addGeofence(LatLng latLng) {
        Disposable disposable = geofenceManager.addGeofence(createNewGeofence(latLng))
                .subscribeOn(Schedulers.io())
                .subscribe(geofence -> Timber.i("Geofence added: %s", geofence),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private Geofence createNewGeofence(LatLng latLng) {
        return Geofence.create(mapOptions.geofenceCreatedName(), latLng, mapOptions.geofenceCreatedRadius(), true);
    }

    @Override
    public void unbindView() {
        super.unbindView();

        disposableContainer.clear();
    }
}
