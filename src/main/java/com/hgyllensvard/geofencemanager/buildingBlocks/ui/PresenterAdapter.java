package com.hgyllensvard.geofencemanager.buildingBlocks.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

public abstract class PresenterAdapter<T extends View> implements Presenter<T> {

    protected T viewActions;

    public PresenterAdapter() {
    }

    @Override
    @CallSuper
    public void bindView(@NonNull T viewActions) {
        this.viewActions = viewActions;
    }

    @Override
    @CallSuper
    public void unbindView() {
        viewActions = null;
    }
}
