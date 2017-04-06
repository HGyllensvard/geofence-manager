package com.hgyllensvard.geofencemanager.geofence.edit.displayGeofence;


import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.edit.map.geofenceView.GeofenceView;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceViewManager;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceViewUpdate;
import com.hgyllensvard.geofencemanager.geofence.edit.map.MapCameraManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DisplayGeofencePresenter extends PresenterAdapter<DisplayGeofenceViews> {

    private final GeofenceViewManager geofenceViewManager;
    private final MapCameraManager mapCameraManager;

    final CompositeDisposable disposableContainer;

    @Inject
    public DisplayGeofencePresenter(
            GeofenceViewManager geofenceViewManager,
            MapCameraManager mapCameraManager
    ) {
        this.geofenceViewManager = geofenceViewManager;
        this.mapCameraManager = mapCameraManager;

        disposableContainer = new CompositeDisposable();
    }

    @Override
    public void bindView(@NonNull DisplayGeofenceViews view) {
        super.bindView(view);

        Disposable disposable = view.displayMap()
                .doOnNext(ignored -> moveToUserPosition())
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> subscribeExistingGeofences(),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    @Override
    public void unbindView() {
        super.unbindView();

        disposableContainer.clear();
    }

    private void moveToUserPosition() {
        view.moveCameraTo(mapCameraManager.userPosition());
    }

    private void subscribeExistingGeofences() {
        Disposable disposable = geofenceViewManager.observeGeofenceViews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {
                    Timber.w(throwable);
                    return Flowable.just(GeofenceViewUpdate.EMPTY_UPDATE);
                })
                .subscribe(geofences -> {
                            sendUpdatedGeofencesToView(geofences.selectedGeofenceViews());
                            sendRemovedGeofenceViewsToView(geofences.removedGeofenceViews());
                        },
                        Timber::e);

        disposableContainer.add(disposable);
    }

    private void sendUpdatedGeofencesToView(List<GeofenceView> geofenceViews) {
        Timber.d("Updating geofences: %s", geofenceViews);
        if (!geofenceViews.isEmpty()) {
            view.displayGeofenceViews(geofenceViews);
        }
    }

    private void sendRemovedGeofenceViewsToView(List<GeofenceView> geofenceViews) {
        Timber.d("Removing geofences: %s", geofenceViews);
        if (!geofenceViews.isEmpty()) {
            view.removeGeofenceViews(geofenceViews);
        }
    }
}
