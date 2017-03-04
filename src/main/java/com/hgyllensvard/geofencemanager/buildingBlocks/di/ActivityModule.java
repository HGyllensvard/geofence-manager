package com.hgyllensvard.geofencemanager.buildingBlocks.di;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    AppCompatActivity providesAppCompatActivity() {
        return activity;
    }
}
