package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceActionResult;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceMapOptions;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddGeofencePresenter extends PresenterAdapter<AddGeofenceViews> {

    private final GeofenceMapOptions mapOptions;
    private final SelectedGeofence selectedGeofence;

    protected final GeofenceManager geofenceManager;

    final CompositeDisposable disposableContainer;

    @Inject
    public AddGeofencePresenter(
            GeofenceManager geofenceManager,
            GeofenceMapOptions mapOptions,
            SelectedGeofence selectedGeofence
    ) {
        this.geofenceManager = geofenceManager;
        this.mapOptions = mapOptions;
        this.selectedGeofence = selectedGeofence;

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
                .flatMap(latLng -> shouldAddNewGeofence()
                        .filter(t -> true)
                        .map(ignored -> latLng))
                .doOnError(Timber::e)
                .retry()
                .subscribe(this::addGeofence);

        disposableContainer.add(disposable);
    }

    private void addGeofence(LatLng latLng) {
        Disposable disposable = geofenceManager.addGeofence(createNewGeofence(latLng))
                .subscribeOn(Schedulers.io())
                .doOnSuccess(geofenceActionResult -> Timber.v("Add geofence result: %s", geofenceActionResult))
                .filter(GeofenceActionResult::success)
                .doOnSuccess(geofenceActionResult -> selectedGeofence.updatedSelectedGeofence(
                        geofenceActionResult.geofence()
                                .id()))
                .subscribe(geofence -> Timber.i("Geofence added: %s", geofence),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private Geofence createNewGeofence(LatLng latLng) {
        return Geofence.create(mapOptions.geofenceCreatedName(), latLng, mapOptions.geofenceCreatedRadius(), true);
    }

    /**
     * If the Geofence exist in the database it means the geofence that was
     * added before still exist, so filter that observable so another one isn't created
     */
    private Observable<Boolean> shouldAddNewGeofence() {
        return selectedGeofence.observeSelectedGeofence()
                .flatMap(geofenceId -> geofenceManager.getGeofence(geofenceId)
                        .onErrorReturn(throwable -> Geofence.sDummyGeofence)
                        .doOnSuccess(geofence -> Timber.v("Fetched geofence: %s", geofence))
                        .filter(geofence -> !geofence.equals(Geofence.sDummyGeofence))
                        .map(geofence -> true)
                        .toObservable());
    }

    @Override
    public void unbindView() {
        super.unbindView();

        disposableContainer.clear();
    }
}
