package com.hgyllensvard.geofencemanager.buildingBlocks.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

public abstract class PresenterAdapter<T extends View> implements Presenter<T> {

    protected T view;

    public PresenterAdapter() {
    }

    @Override
    @CallSuper
    public void bindView(@NonNull T view) {
        this.view = view;
    }

    @Override
    @CallSuper
    public void unbindView() {
        view = null;
    }
}
