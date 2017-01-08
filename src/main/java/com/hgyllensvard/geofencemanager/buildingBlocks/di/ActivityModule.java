package com.hgyllensvard.geofencemanager.buildingBlocks.di;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule extends ContextModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        super(activity);
        
        this.activity = activity;
    }

    @PerActivity
    @Provides
    AppCompatActivity providesAppCompatActivity() {
        return activity;
    }
}
