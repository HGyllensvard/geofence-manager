package com.hgyllensvard.geofencemanager.geofence.editGeofence;


import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.hgyllensvard.geofencemanager.GeofenceManagerActivity;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.R2;
import com.hgyllensvard.geofencemanager.geofence.map.MapView;
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

    @BindView(R2.id.geofence_rename)
    EditText renameGeofence;

    @Inject
    EditGeofencePresenter editGeofencePresenter;

    @Inject
    MapView mapView;

    private Unbinder unbinder;

    private Observable<Boolean> renameObservable;
    private Observable<Boolean> deleteFlowable;

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

        injectDependencies();

        inflate(getContext(), R.layout.edit_geofence_view, this);
        unbinder = ButterKnife.bind(this);

        renameObservable = RxJavaInterop.toV2Observable(RxView.clicks(renameGeofenceMenuAction)
                .map(aVoid -> true));

        deleteFlowable = RxJavaInterop.toV2Observable(RxView.clicks(deleteGeofenceMenuAction)
                .map(aVoid -> true));

        editGeofencePresenter.bindView(this);
    }

    private void injectDependencies() {
        if (getContext() instanceof AppCompatActivity) {
            ((GeofenceManagerActivity) getContext()).getGeofenceComponent()
                    .inject(this);
        } else {
            throw new IllegalStateException("Activity not build to support this view");
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
        renameGeofence.setVisibility(View.GONE);
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

    private Observable<Boolean> observeRenameGeofence() {
        return renameObservable;
    }

    @Override
    public Observable<Boolean> observeDeleteGeofence() {
        return deleteFlowable;
    }

    private void displayRenameGeofence(String name) {
        renameGeofence.setVisibility(View.VISIBLE);
        renameGeofence.setText(name);

        displayKeyboard();

        renameGeofence.setSelection(name.length());
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
