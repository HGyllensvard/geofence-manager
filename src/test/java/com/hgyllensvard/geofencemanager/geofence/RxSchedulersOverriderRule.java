package com.hgyllensvard.geofencemanager.geofence;


import org.junit.BeforeClass;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RxSchedulersOverriderRule implements TestRule {

    @BeforeClass
    public static void setupClass() {
        Timber.i("Overriding Android scheduler");
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Timber.i("Overriding IO scheduler");
                RxJavaPlugins.setIoSchedulerHandler(schedulers -> Schedulers.trampoline());

                base.evaluate();

                RxJavaPlugins.reset();
                RxAndroidPlugins.reset();
            }
        };
    }
}
