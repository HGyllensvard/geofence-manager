package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.RxPresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.toolbar.ToolbarTitle;
import com.hgyllensvard.geofencemanager.toolbar.ToolbarTitleManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EditGeofencePresenter extends RxPresenterAdapter<EditGeofenceViews> {

    private final SelectedGeofenceId selectedGeofenceId;
    private final ToolbarTitleManager toolbarTitleManager;
    private final SelectedGeofence selectedGeofence;
    private final GeofenceManager geofenceManager;

    @Inject
    EditGeofencePresenter(
            SelectedGeofenceId selectedGeofenceId,
            ToolbarTitleManager toolbarTitleManager,
            SelectedGeofence selectedGeofence,
            GeofenceManager geofenceManager
    ) {
        this.geofenceManager = geofenceManager;
        this.selectedGeofenceId = selectedGeofenceId;
        this.toolbarTitleManager = toolbarTitleManager;
        this.selectedGeofence = selectedGeofence;
    }

    @Override
    public void bindView(@NonNull EditGeofenceViews view) {
        super.bindView(view);

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
            Geofence geofence = view.getGeofencePosition(selectedGeofenceId.selectedGeofenceId());

            attemptUpdateGeofence(toolbarTitle, geofence);
        }

        super.unbindView();
    }

    private void attemptUpdateGeofence(ToolbarTitle toolbarTitle, Geofence geofence) {
        if (geofence == null) {
            Timber.w("Geofence was null, can't save the geofence changes.");
        } else {
            if (toolbarTitle != null) {
                geofence = geofence.withName(toolbarTitle.title());
            }

            geofenceManager.updateGeofence(geofence)
                    .subscribeOn(Schedulers.io())
                    .subscribe(actionResult -> Timber.i("Result for updating geofence: %s", actionResult),
                            Timber::e);
        }
    }

    private void subscribeSelectedGeofence() {
        Disposable disposable = selectedGeofence.observeSelectedGeofence()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(geofenceState -> {
                    Timber.d("GeofenceSelected: %s geofence: %s", geofenceState.validGeofence(), geofenceState.geofence());
                    if (geofenceState.validGeofence()) {
                        view.displaySelectedGeofenceOptions();
                    } else {
                        view.hideSelectedGeofenceOptions();
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
