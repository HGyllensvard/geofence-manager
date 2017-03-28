package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.RxPresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.toolbar.EditableTitleToolbarPresenter;
import com.hgyllensvard.geofencemanager.toolbar.ToolbarTitle;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class EditGeofencePresenter extends RxPresenterAdapter<EditGeofenceViews> {

    private final EditGeofencePresenterManager editGeofencePresenterManager;
    private final SelectedGeofence selectedGeofence;
    private final EditableTitleToolbarPresenter editableTitleToolbarPresenter;

    @Inject
    public EditGeofencePresenter(
            EditGeofencePresenterManager editGeofencePresenterManager,
            SelectedGeofence selectedGeofence,
            EditableTitleToolbarPresenter editableTitleToolbarPresenter
    ) {
        this.editGeofencePresenterManager = editGeofencePresenterManager;
        this.selectedGeofence = selectedGeofence;
        this.editableTitleToolbarPresenter = editableTitleToolbarPresenter;
    }

    @Override
    public void bindView(@NonNull EditGeofenceViews view) {
        super.bindView(view);

        view.hideSelectedGeofenceOptions();

        Disposable displayMap = view.displayMap()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                    subscribeSelectedGeofence();
                    subscribeDeleteGeofence();
                }, Timber::e);

        disposables.add(displayMap);
    }

    @Override
    public void unbindView() {
        if (view != null && selectedGeofence.isGeofenceSelected()) {
            ToolbarTitle toolbarTitle = editableTitleToolbarPresenter.title();
            LatLng geofenceMarker = view.getGeofencePosition(selectedGeofence.selectedGeofence());

            if (toolbarTitle == null) {
                Timber.w("Toolbar title was null, don't save the geofence changes.");
            } else {
                editGeofencePresenterManager.updateSelectedGeofence(toolbarTitle.title(), geofenceMarker);
            }
        }

        super.unbindView();
    }

    private void subscribeSelectedGeofence() {
        Disposable disposable = editGeofencePresenterManager.observeSelectedGeofence()
                .doOnNext(geofence -> displayGeofenceName(geofence.name()))
                .doOnNext(geofence -> view.displaySelectedGeofenceOptions())
                .subscribe(geofenceId -> {
                }, Timber::e);

        disposables.add(disposable);
    }

    private void displayGeofenceName(String geofenceName) {
        Timber.v("Displaying geofence title: %s", geofenceName);
        view.displayGeofenceName(geofenceName);
    }

    private void subscribeDeleteGeofence() {
        Disposable disposable = view.observeDeleteGeofence()
                .flatMap(ignored -> editGeofencePresenterManager.deleteSelectedGeofence()
                        .toObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(successfullyDeletedGeofence -> {
                    if (successfullyDeletedGeofence) {
                        view.hideSelectedGeofenceOptions();
                        view.exitView();
                    }
                    Timber.v("Successfully deleted selected geofence? : %s", successfullyDeletedGeofence);
                }, Timber::e);

        disposables.add(disposable);
    }
}
