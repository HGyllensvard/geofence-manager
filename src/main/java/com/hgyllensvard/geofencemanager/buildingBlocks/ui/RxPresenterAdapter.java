package com.hgyllensvard.geofencemanager.buildingBlocks.ui;


import io.reactivex.disposables.CompositeDisposable;

public abstract class RxPresenterAdapter<T extends View> extends PresenterAdapter<T> {

    protected CompositeDisposable disposables;

    protected RxPresenterAdapter() {
        disposables = new CompositeDisposable();
    }

    @Override
    public void unbindView() {
        super.unbindView();

        disposables.clear();
    }
}
