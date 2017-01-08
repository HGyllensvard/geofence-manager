package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.annotation.ColorInt;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeofenceMapOptions {

    public static final int DEFAULT_PREDEFINED_GEOFENCE_RADIUS = 50;
    public static final String DEFAULT_PREDEFINED_GEOFENCE_NAME = "Geofence";

    @ColorInt
    public abstract int strokeColor();

    @ColorInt
    public abstract int fillColor();

    /**
     * @return Default geofence radius in meters
     */
    public abstract int defaultRadius();

    /**
     * @return Default name a geofence is given when created, several geofences can have the same
     * name, they are uniquely distinguishably by their ID.
     */
    public abstract String defaultName();

    public static GeofenceMapOptions.Builder create() {
        return new AutoValue_GeofenceMapOptions.Builder()
                .defaultRadius(DEFAULT_PREDEFINED_GEOFENCE_RADIUS)
                .defaultName(DEFAULT_PREDEFINED_GEOFENCE_NAME);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract GeofenceMapOptions.Builder strokeColor(@ColorInt int strokeColor);

        public abstract GeofenceMapOptions.Builder fillColor(@ColorInt int fillColor);

        public abstract GeofenceMapOptions.Builder defaultRadius(int defaultRadius);

        public abstract GeofenceMapOptions.Builder defaultName(String defaultGeofenceName);

        public abstract GeofenceMapOptions build();
    }
}
