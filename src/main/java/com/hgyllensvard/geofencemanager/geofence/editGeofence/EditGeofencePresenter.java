package com.hgyllensvard.geofencemanager.geofence.editGeofence;


import android.support.annotation.NonNull;

import com.hgyllensvard.geofencemanager.buildingBlocks.ui.PresenterAdapter;
import com.hgyllensvard.geofencemanager.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.map.GeofenceMarkerManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class EditGeofencePresenter extends PresenterAdapter<EditGeofenceViews> {

    private static final int NO_SELECTION = -1;
    private static final int SELECT_DEBOUNCE_TIMER = 1;
    private static final TimeUnit SELECT_DEBOUNCE_TIMER_UNIT = TimeUnit.SECONDS;

    private final GeofenceManager geofenceManager;
    private final GeofenceMarkerManager geofenceMarkerManager;

    private final CompositeDisposable disposableContainer;

    private long selectedGeofenceId = NO_SELECTION;

    public EditGeofencePresenter(
            GeofenceManager geofenceManager,
            GeofenceMarkerManager geofenceMarkerManager
    ) {
        this.geofenceManager = geofenceManager;
        this.geofenceMarkerManager = geofenceMarkerManager;

        disposableContainer = new CompositeDisposable();
    }

    @Override
    public void bindView(@NonNull EditGeofenceViews view) {
        super.bindView(view);

        view.displayMap()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                            subscribeSelectedGeofence();
                            subscribeDeselectGeofence();
                            subscribeRenameGeofence();
                            subscribeDeleteGeofence();
                        },
                        Timber::e);
    }

    private void subscribeSelectedGeofence() {
        Disposable disposable = view.observeMarkerSelected()
                .debounce(SELECT_DEBOUNCE_TIMER, SELECT_DEBOUNCE_TIMER_UNIT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(marker -> {
                    selectedGeofenceId = geofenceMarkerManager.findGeofenceId(marker.getId());
                    view.displaySelectedGeofenceOptions();
                    Timber.v("Selected Geofence with id: %s", selectedGeofenceId);
                }, Timber::e);

        disposableContainer.add(disposable);
    }

    private void subscribeDeselectGeofence() {
        Disposable disposable = view.observeCameraStartedMoving()
                .observeOn(AndroidSchedulers.mainThread())
                .filter(integer -> selectedGeofenceId != NO_SELECTION)
                .subscribe(reason -> {
                    Timber.v("Geofence deselected");
                    selectedGeofenceId = NO_SELECTION;
                    view.hideSelectedGeofenceOptions();
                }, Timber::e);

        disposableContainer.add(disposable);
    }

    private void subscribeRenameGeofence() {

    }

    private void subscribeDeleteGeofence() {
        Disposable disposable = geofenceManager.removeGeofence(selectedGeofenceId)
                .subscribe(deletedGeofence -> Timber.v("Removed geofence: %s", deletedGeofence),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    // TODO Change implementation, don't send in the geofence, use the selected ID to fetch and use that.
    private void renameSelectedGeofence(
            Geofence geofence,
            String newName
    ) {
        Disposable disposable = geofenceManager.updateGeofence(geofence, geofence.withName(newName))
                .subscribe(updatedGeofence -> Timber.v("Updated geofence to: %s", updatedGeofence),
                        Timber::e);

        disposableContainer.add(disposable);
    }

    @Override
    public void unbindView() {
        super.unbindView();
    }
}
