package com.hgyllensvard.geofencemanager.toolbar;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.buildingBlocks.ui.RxPresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@PerActivity
public class EditableTitleToolbarPresenter extends RxPresenterAdapter<EditableToolbarView> {

    private SelectedGeofence selectedGeofence;
    private GeofenceManager geofenceManager;

    private ToolbarTitle toolbarTitle;

    @Inject
    public EditableTitleToolbarPresenter(
            SelectedGeofence selectedGeofence,
            GeofenceManager geofenceManager
    ) {
        this.selectedGeofence = selectedGeofence;
        this.geofenceManager = geofenceManager;
    }

    @Override
    public void bindView(@NonNull EditableToolbarView view) {
        super.bindView(view);

        subscribeToolbarTitleChanges();

        setSelectedGeofenceNameAsTitle();
    }

    @Nullable
    public ToolbarTitle title() {
        return toolbarTitle;
    }

    private void subscribeToolbarTitleChanges() {
        Disposable titleChangeDisposable = view.observeTitle()
                .subscribe(toolbarTitle -> this.toolbarTitle = toolbarTitle,
                        Timber::e);

        disposables.add(titleChangeDisposable);
    }

    private void setSelectedGeofenceNameAsTitle() {
        geofenceManager.getGeofence(selectedGeofence.selectedGeofence())
                .filter(geofence -> !geofence.equals(Geofence.sDummyGeofence))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(geofence -> view.title(geofence.name()),
                        Timber::e);
    }
}
