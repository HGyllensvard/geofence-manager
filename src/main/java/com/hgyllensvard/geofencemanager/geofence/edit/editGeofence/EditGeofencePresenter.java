package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.RxPresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.toolbar.ToolbarTitle;
import com.hgyllensvard.geofencemanager.toolbar.ToolbarTitleManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class EditGeofencePresenter extends RxPresenterAdapter<EditGeofenceViews> {

    private final EditGeofencePresenterManager editGeofencePresenterManager;
    private final SelectedGeofenceId selectedGeofenceId;
    private final ToolbarTitleManager toolbarTitleManager;
    private final SelectedGeofence selectedGeofence;

    @Inject
    EditGeofencePresenter(
            EditGeofencePresenterManager editGeofencePresenterManager,
            SelectedGeofenceId selectedGeofenceId,
            ToolbarTitleManager toolbarTitleManager,
            SelectedGeofence selectedGeofence
    ) {
        this.editGeofencePresenterManager = editGeofencePresenterManager;
        this.selectedGeofenceId = selectedGeofenceId;
        this.toolbarTitleManager = toolbarTitleManager;
        this.selectedGeofence = selectedGeofence;
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
        if (view != null && selectedGeofenceId.isGeofenceSelected()) {
            ToolbarTitle toolbarTitle = toolbarTitleManager.title();
            LatLng position = view.getGeofencePosition(selectedGeofenceId.selectedGeofenceId());

            if (toolbarTitle == null) {
                Timber.w("Toolbar title was null, don't save the geofence changes.");
            } else {
                editGeofencePresenterManager.updateSelectedGeofence(toolbarTitle.title(), position);
            }
        }

        super.unbindView();
    }

    private void subscribeSelectedGeofence() {
        Disposable disposable = selectedGeofence.observeSelectedGeofence()
                .subscribe(geofence -> {
                    if (geofence.equals(Geofence.sDummyGeofence)) {
                        view.hideSelectedGeofenceOptions();
                    } else {
                        view.displaySelectedGeofenceOptions();
                    }
                }, Timber::e);

        disposables.add(disposable);
    }

    private void subscribeDeleteGeofence() {
        Disposable disposable = view.observeDeleteGeofence()
                .flatMap(ignored -> selectedGeofence.delete()
                        .toObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(successfullyDeletedGeofence -> {
                    if (successfullyDeletedGeofence) {
                        view.exitView();
                    }
                    Timber.v("Successfully deleted selected geofence? : %s", successfullyDeletedGeofence);
                }, Timber::e);

        disposables.add(disposable);
    }
}
