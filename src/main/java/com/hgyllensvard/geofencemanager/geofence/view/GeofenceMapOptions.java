package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeofenceMapOptions {

    @ColorInt
    public abstract int strokeColor();

    @ColorInt
    public abstract int fillColor();

    public static GeofenceMapOptions create(
            @ColorInt int strokeColor,
            @ColorInt int fillColor
    ) {
        return new AutoValue_GeofenceMapOptions(strokeColor, fillColor);
    }
}
