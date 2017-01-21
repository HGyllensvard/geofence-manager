package com.hgyllensvard.geofencemanager.geofence.displayGeofence;


import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.MapCameraManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceViewManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DisplayGeofencePresenter extends PresenterAdapter<DisplayGeofenceViews> {

    private final GeofenceManager geofenceManager;
    private final MapCameraManager mapCameraManager;
    private final GeofenceViewManager geofenceViewManager;

    private final CompositeDisposable disposableContainer;

    public DisplayGeofencePresenter(
            GeofenceManager geofenceManager,
            MapCameraManager mapCameraManager,
            GeofenceViewManager geofenceViewManager
    ) {
        this.geofenceManager = geofenceManager;
        this.mapCameraManager = mapCameraManager;
        this.geofenceViewManager = geofenceViewManager;

        disposableContainer = new CompositeDisposable();
    }

    @Override
    public void bindView(@NonNull DisplayGeofenceViews view) {
        super.bindView(view);

        view.displayMap()
                .doOnNext(ignored -> zoomToUserPosition())
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> subscribeExistingGeofences(),
                        Timber::e);
    }

    private void zoomToUserPosition() {
        view.animateCameraTo(mapCameraManager.userPosition());
    }

    private void subscribeExistingGeofences() {
        Disposable disposable = geofenceManager.observeGeofences()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(geofences -> {
                            Timber.v("Displaying geofences: %s", geofences);
                            view.displayGeofences(geofences);
                        },
                        Timber::e
                );

        disposableContainer.add(disposable);
    }

    @Override
    public void unbindView() {
        super.unbindView();

        disposableContainer.clear();
    }
}
