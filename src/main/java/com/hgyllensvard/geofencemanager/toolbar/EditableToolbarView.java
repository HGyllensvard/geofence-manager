package com.hgyllensvard.geofencemanager.toolbar;


import com.hgyllensvard.geofencemanager.buildingBlocks.ui.View;

import io.reactivex.Observable;

public interface EditableToolbarView extends View {

    Observable<String> observeTitle();

    ToolbarTitle title();

    void title(String name);

    void hideTitle();
}
