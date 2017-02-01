package com.hgyllensvard.geofencemanager.buildingBlocks.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.Component;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}