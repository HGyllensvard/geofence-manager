package com.hgyllensvard.geofencemanager.geofence.editGeofence;


import android.app.Activity;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hgyllensvard.geofencemanager.R2;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Flowable;

public class EditGeofenceView implements EditGeofenceViews {

    private static final int SIMULATE_CLICK_DELAY = 150;

    @BindView(R2.id.edit_geofence_menu)
    FloatingActionMenu editGeofenceMenu;

    @BindView(R2.id.geofence_menu_rename_geofence)
    FloatingActionButton renameGeofenceMenuAction;

    @BindView(R2.id.geofence_menu_delete_geofence)
    FloatingActionButton deleteGeofenceMenuAction;

    @BindView(R2.id.geofence_rename)
    EditText renameGeofence;

    private final Activity activity;
    private final MapView mapView;

    private Unbinder unbinder;

    private Flowable<Boolean> renameObservable;
    private Flowable<Boolean> deleteFlowable;

    public EditGeofenceView(
            Activity activity,
            MapView mapView
    ) {
        this.activity = activity;
        this.mapView = mapView;

        unbinder = ButterKnife.bind(this, activity);

        renameObservable = RxJavaInterop.toV2Flowable(RxView.clicks(renameGeofenceMenuAction)
                .map(aVoid -> true));

        deleteFlowable = RxJavaInterop.toV2Flowable(RxView.clicks(deleteGeofenceMenuAction)
                .map(aVoid -> true));

        editGeofenceMenu.hideMenuButton(false);
    }

    @Override
    public Flowable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public Flowable<Long> observeGeofenceSelected() {
        return mapView.observeGeofenceSelected();
    }

    @Override
    public Flowable<Integer> observeCameraStartedMoving() {
        return mapView.observeCameraStartMoving();
    }

    @Override
    public void hideSelectedGeofenceOptions() {
        editGeofenceMenu.hideMenuButton(true);
        renameGeofence.setVisibility(View.GONE);
        renameGeofence.setText("");
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

    @Override
    public void displayRenameGeofence(String name) {
        renameGeofence.setVisibility(View.VISIBLE);
        renameGeofence.setText(name);

        displayKeyboard();

        renameGeofence.setSelection(name.length());
    }

    @Override
    public void removeMarkerFromMap(long selectedGeofenceId) {

    }

    // Ugly solution, could not get the keyboard to be displayed properly.
    // Found at: http://stackoverflow.com/questions/5105354/how-to-show-soft-keyboard-when-edittext-is-focused
    private void displayKeyboard() {
        renameGeofence.postDelayed(() -> {
            renameGeofence.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            renameGeofence.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }, SIMULATE_CLICK_DELAY);
    }
}
