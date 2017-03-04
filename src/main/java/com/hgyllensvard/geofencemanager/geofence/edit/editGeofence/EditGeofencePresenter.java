package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class EditGeofencePresenter extends PresenterAdapter<EditGeofenceViews> {

    private final GeofenceManager geofenceManager;
    private final SelectedGeofence selectedGeofence;

    private final CompositeDisposable disposableContainer;

    @Inject
    public EditGeofencePresenter(
            GeofenceManager geofenceManager,
            SelectedGeofence selectedGeofence
    ) {
        this.geofenceManager = geofenceManager;
        this.selectedGeofence = selectedGeofence;

        disposableContainer = new CompositeDisposable();
    }

    @Override
    public void bindView(@NonNull EditGeofenceViews view) {
        super.bindView(view);

        view.hideSelectedGeofenceOptions();

        view.displayMap()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
//                    subscribeSelectedGeofence();
//                    subscribeDeselectGeofence();
//                    subscribeDeleteGeofence();
                }, Timber::e);
    }

//    private void subscribeSelectedGeofence() {
//        Disposable disposable = view.observeGeofenceSelected()
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter(selectedGeofence::isCurrentlySelected)
//                .subscribe(geofenceId -> {
////                    selectedGeofence.;
//                    view.displaySelectedGeofenceOptions();
//                    Timber.v("Selected Geofence with id: %s", selectedGeofenceId);
//                }, Timber::e);
//
//        disposableContainer.add(disposable);
//    }
//
//    private void subscribeDeselectGeofence() {
//        Disposable disposable = view.observeCameraStartedMoving()
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .filter(integer -> selectedGeofenceId != NO_SELECTION)
//                .subscribe(reason -> {
//                    Timber.v("Geofence deselected");
//                    selectedGeofenceId = NO_SELECTION;
//                    view.hideSelectedGeofenceOptions();
//                }, Timber::e);
//
//        disposableContainer.add(disposable);
//    }
//
//    private void subscribeDeleteGeofence() {
//        Disposable disposable = view.observeDeleteGeofence()
//                .observeOn(Schedulers.io())
//                .filter(integer -> selectedGeofenceId != NO_SELECTION)
//                .flatMap(ignored -> geofenceManager.removeGeofence(selectedGeofenceId)
//                        .toObservable())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(successfullyDeletedGeofence -> {
//                    if (successfullyDeletedGeofence) {
//                        view.hideSelectedGeofenceOptions();
//                        selectedGeofenceId = NO_SELECTION;
//                    }
//
//                    Timber.v("Deleted geofence: %s", successfullyDeletedGeofence);
//                }, Timber::e);
//
//        disposableContainer.add(disposable);
//    }

    @Override
    public void unbindView() {
        super.unbindView();
    }
}
