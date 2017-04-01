package com.hgyllensvard.geofencemanager;

import android.app.Application;

import timber.log.Timber;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setupLogging();
    }

    protected void setupLogging() {
        Timber.uprootAll();
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                final String msg = tag + " " + message;
                if (t != null) {
                    System.err.println(msg + ": " + t);
                } else {
                    System.out.println(msg);
                }
            }
        });
    }
}
