package com.hgyllensvard.geofencemanager.toolbar;


import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.RxPresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofence;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@PerActivity
public class EditableTitleToolbarPresenter extends RxPresenterAdapter<EditableToolbarView> {

    private final SelectedGeofence selectedGeofence;
    private final ToolbarTitleManager toolbarTitleManager;

    @Inject
    EditableTitleToolbarPresenter(
            SelectedGeofence selectedGeofence,
            ToolbarTitleManager toolbarTitleManager
    ) {
        this.selectedGeofence = selectedGeofence;
        this.toolbarTitleManager = toolbarTitleManager;
    }

    @Override
    public void bindView(@NonNull EditableToolbarView view) {
        super.bindView(view);

        subscribeToolbarTitleChanges();

        setSelectedGeofenceNameAsTitle();
    }

    private void subscribeToolbarTitleChanges() {
        Disposable titleChangeDisposable = view.observeTitle()
                .map(ToolbarTitle::create)
                .subscribe(toolbarTitleManager::title,
                        Timber::e);

        disposables.add(titleChangeDisposable);
    }

    private void setSelectedGeofenceNameAsTitle() {
        Disposable disposable = selectedGeofence.observeValidSelectedGeofence()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(geofence -> view.title(geofence.name()),
                        Timber::e);

        disposables.add(disposable);
    }
}
