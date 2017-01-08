package com.hgyllensvard.geofencemanager.buildingBlocks.di;

import android.content.Context;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @PerActivity
    @Provides
    Context providesContext() {
        return context;
    }

    @PerActivity
    @Provides
    LocationManager providesAndroidLocationManager() {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
