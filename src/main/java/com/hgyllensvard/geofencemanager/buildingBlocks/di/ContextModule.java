package com.hgyllensvard.geofencemanager.buildingBlocks.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context providesContext() {
        return context;
    }
}
