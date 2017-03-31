package com.hgyllensvard.geofencemanager.toolbar;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class ToolbarTitleManager {

    private BehaviorSubject<ToolbarTitle> toolbarTitleBehaviorSubject;

    @Inject
    public ToolbarTitleManager() {
        toolbarTitleBehaviorSubject = BehaviorSubject.create();
    }

    public void title(ToolbarTitle toolbarTitle) {
        toolbarTitleBehaviorSubject.onNext(toolbarTitle);
    }

    public ToolbarTitle title() {
        return toolbarTitleBehaviorSubject.getValue();
    }

    public Observable<ToolbarTitle> observeToolbarTitle() {
        return toolbarTitleBehaviorSubject
                .distinctUntilChanged();
    }
}
