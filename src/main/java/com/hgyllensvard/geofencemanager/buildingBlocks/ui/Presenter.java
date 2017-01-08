package com.hgyllensvard.geofencemanager.buildingBlocks.ui;

import android.support.annotation.NonNull;

public interface Presenter<T extends View> {

    void bindView(@NonNull T viewActions);

    void unbindView();
}
