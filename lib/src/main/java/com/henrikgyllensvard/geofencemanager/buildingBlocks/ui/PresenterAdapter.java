package com.henrikgyllensvard.geofencemanager.buildingBlocks.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

public abstract class PresenterAdapter<T extends View> implements Presenter {

    protected T viewActions;

    public PresenterAdapter(@NonNull T viewActions) {
        this.viewActions = viewActions;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    @CallSuper
    public void onDestroy() {
        viewActions = null;
    }
}
