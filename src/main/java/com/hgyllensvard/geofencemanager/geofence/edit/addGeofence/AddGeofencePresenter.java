package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.RxPresenterAdapter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddGeofencePresenter extends RxPresenterAdapter<AddGeofenceViews> {

    private final AddGeofenceManager addGeofenceManager;

    @Inject
    public AddGeofencePresenter(
            AddGeofenceManager addGeofenceManager
    ) {
        this.addGeofenceManager = addGeofenceManager;
    }

    @Override
    public void bindView(@NonNull AddGeofenceViews view) {
        super.bindView(view);

        Disposable disposable = view.displayMap()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> subscribeLongClick(),
                        Timber::e);

        disposables.add(disposable);
    }

    private void subscribeLongClick() {
        Disposable disposable = view.observerLongClick()
                .observeOn(Schedulers.io())
                .doOnNext(latLng -> Timber.v("Attempting to add new geofence at: %s", latLng))
                .flatMap(latLng -> addGeofenceManager.addGeofence(latLng)
                        .toObservable())
                .doOnError(Timber::e)
                .retry()
                .subscribe(geofenceAdded -> {
                        },
                        Timber::e);

        disposables.add(disposable);
    }

    @VisibleForTesting
    protected CompositeDisposable disposables() {
        return disposables;
    }
}
