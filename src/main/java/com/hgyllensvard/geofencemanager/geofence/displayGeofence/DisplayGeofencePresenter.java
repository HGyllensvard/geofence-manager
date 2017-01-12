package com.hgyllensvard.geofencemanager.geofence.displayGeofence;


import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.MapCameraManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMarkerManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DisplayGeofencePresenter extends PresenterAdapter<DisplayGeofenceViews> {

    private final GeofenceManager geofenceManager;
    private final MapCameraManager mapCameraManager;
    private final GeofenceMarkerManager geofenceMarkerManager;

    private final CompositeDisposable disposableContainer;

    public DisplayGeofencePresenter(
            GeofenceManager geofenceManager,
            MapCameraManager mapCameraManager,
            GeofenceMarkerManager geofenceMarkerManager
    ) {
        this.geofenceManager = geofenceManager;
        this.mapCameraManager = mapCameraManager;
        this.geofenceMarkerManager = geofenceMarkerManager;

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
                .map(geofenceMarkerManager::createMarkers)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(geofenceMarkers -> Timber.v("Displaying geofences markers: %s", geofenceMarkers))
                .subscribe(markers -> view.displayGeofences(markers),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    @Override
    public void unbindView() {
        super.unbindView();

        disposableContainer.clear();
    }
}
