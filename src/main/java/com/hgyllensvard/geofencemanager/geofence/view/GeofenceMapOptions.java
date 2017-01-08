package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.annotation.ColorInt;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeofenceMapOptions {

    @ColorInt
    public abstract int strokeColor();

    @ColorInt
    public abstract int fillColor();

    /**
     * @return Default geofence radius in meters
     */
    public abstract int defaultRadius();

    public static GeofenceMapOptions create(
            @ColorInt int strokeColor,
            @ColorInt int fillColor,
            int defaultRadius
    ) {
        return new AutoValue_GeofenceMapOptions(strokeColor, fillColor, defaultRadius);
    }
}
