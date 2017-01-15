package com.hgyllensvard.geofencemanager.geofence.editGeofence;


import android.app.Activity;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.model.Marker;
import com.hgyllensvard.geofencemanager.R2;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;

public class EditGeofenceView implements EditGeofenceViews {

    @BindView(R2.id.edit_geofence_menu)
    FloatingActionMenu editGeofenceMenu;

    @BindView(R2.id.geofence_menu_rename_geofence)
    FloatingActionButton renameGeofence;

    @BindView(R2.id.geofence_menu_delete_geofence)
    FloatingActionButton deleteGeofence;

    private final MapView mapView;

    private Unbinder unbinder;

    private Flowable<Boolean> renameObservable;
    private Flowable<Boolean> deleteFlowable;

    public EditGeofenceView(
            Activity activity,
            MapView mapView
    ) {
        this.mapView = mapView;

        unbinder = ButterKnife.bind(this, activity);

        renameObservable = RxJavaInterop.toV2Flowable(RxView.clicks(renameGeofence)
                .map(aVoid -> true));

        deleteFlowable = RxJavaInterop.toV2Flowable(RxView.clicks(deleteGeofence)
                .map(aVoid -> true));

        editGeofenceMenu.hideMenuButton(false);
    }

    @Override
    public Flowable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public Flowable<Marker> observeMarkerSelected() {
        return mapView.observeMarkerSelected();
    }

    @Override
    public Flowable<Integer> observeCameraStartedMoving() {
        return mapView.observeCameraStartMoving();
    }

    @Override
    public void hideSelectedGeofenceOptions() {
        editGeofenceMenu.hideMenuButton(true);
    }

    @Override
    public void displaySelectedGeofenceOptions() {
        editGeofenceMenu.showMenuButton(true);
    }

    @Override
    public void destroy() {
        unbinder.unbind();
    }

    @Override
    public Flowable<Boolean> observeRenameGeofence() {
        return renameObservable;
    }

    @Override
    public Flowable<Boolean> observeDeleteGeofence() {
        return deleteFlowable;
    }
}
