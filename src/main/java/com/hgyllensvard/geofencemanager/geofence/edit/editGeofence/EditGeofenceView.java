package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.ActivityFlowManager;
import com.hgyllensvard.geofencemanager.GeofenceManagerInjector;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.R2;
import com.hgyllensvard.geofencemanager.geofence.edit.map.MapView;
import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;

public class EditGeofenceView extends RelativeLayout implements EditGeofenceViews {

    private static final int SIMULATE_CLICK_DELAY = 150;

    @BindView(R2.id.edit_geofence_menu)
    FloatingActionMenu editGeofenceMenu;

    @BindView(R2.id.geofence_menu_rename_geofence)
    FloatingActionButton renameGeofenceMenuAction;

    @BindView(R2.id.geofence_menu_delete_geofence)
    FloatingActionButton deleteGeofenceMenuAction;

    @Inject
    EditGeofencePresenter editGeofencePresenter;

    @Inject
    MapView mapView;

    private Unbinder unbinder;

    private Observable<Boolean> renameObservable;
    private Observable<Boolean> deleteFlowable;

    private ActivityFlowManager activityFlowManager;

    public EditGeofenceView(Context context) {
        super(context);
    }

    public EditGeofenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditGeofenceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        inflate(getContext(), R.layout.edit_geofence_view, this);
        unbinder = ButterKnife.bind(this);

        if (!isInEditMode()) {
            renameObservable = RxJavaInterop.toV2Observable(RxView.clicks(renameGeofenceMenuAction)
                    .map(ignored -> true));

            deleteFlowable = RxJavaInterop.toV2Observable(RxView.clicks(deleteGeofenceMenuAction)
                    .map(ignored -> true));

            injectDependencies();
            setupActivityFlowManager();

            editGeofencePresenter.bindView(this);
        }
    }

    private void setupActivityFlowManager() {
        if (getContext() instanceof ActivityFlowManager) {
            activityFlowManager = (ActivityFlowManager) getContext();
        } else {
            throw new IllegalStateException("Context does not implement: " + ActivityFlowManager.class.getSimpleName());
        }
    }

    private void injectDependencies() {
        if (getContext() instanceof GeofenceManagerInjector) {
            ((GeofenceManagerInjector) getContext()).getGeofenceManagerComponent()
                    .inject(this);
        } else {
            throw new IllegalStateException("Context does not implement: %s" + GeofenceManagerInjector.class.getSimpleName());
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        editGeofencePresenter.unbindView();
        unbinder.unbind();
    }

    @Override
    public Observable<Boolean> displayMap() {
        return mapView.initialiseAndDisplayMap();
    }

    @Override
    public Observable<Long> observeGeofenceSelected() {
        return mapView.observeGeofenceSelected();
    }

    @Override
    public Observable<Integer> observeCameraStartedMoving() {
        return mapView.observeCameraStartMoving();
    }

    @Override
    public void instantlyHideSelectedGeofenceOptions() {
        editGeofenceMenu.hideMenuButton(false);
    }

    @Override
    public void hideSelectedGeofenceOptions() {
        editGeofenceMenu.hideMenuButton(true);
    }

    @Override
    public void displaySelectedGeofenceOptions() {
        editGeofenceMenu.showMenuButton(true);
    }

    private Observable<Boolean> observeRenameGeofence() {
        return renameObservable;
    }

    @Override
    public Observable<Boolean> observeDeleteGeofence() {
        return deleteFlowable;
    }

    @Override
    public void exitView() {
        activityFlowManager.viewAsksForExit();
    }

    @Override
    public String getGeofenceName() {
//        return geofenceName.getText().toString();
        return null;
    }

    @Override
    public LatLng getGeofencePosition(long geofenceId) {
        return mapView.getGeofencePosition(geofenceId);
    }

    @Override
    public void displayGeofenceName(String name) {

//        geofenceName.setText(title);
    }

//    private void displayRenameGeofence(String title) {
//        geofenceName.setVisibility(View.VISIBLE);
//        geofenceName.setText(title);
//
//        displayKeyboard();
//
//        geofenceName.setSelection(title.length());
//    }
//
//    // Ugly solution, could not get the keyboard to be displayed properly.
//    // Found at: http://stackoverflow.com/questions/5105354/how-to-show-soft-keyboard-when-edittext-is-focused
//    private void displayKeyboard() {
//        geofenceName.postDelayed(() -> {
//            geofenceName.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
//            geofenceName.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
//        }, SIMULATE_CLICK_DELAY);
//    }
}
