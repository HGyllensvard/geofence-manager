package com.hgyllensvard.geofencemanager.geofence.edit.map;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

import com.google.auto.value.AutoValue;
import com.hgyllensvard.geofencemanager.R;

@AutoValue
public abstract class GeofenceMapOptions {

    private static final int DEFAULT_PREDEFINED_GEOFENCE_MIN_SIZE = 10;
    private static final int DEFAULT_PREDEFINED_GEOFENCE_MAX_SIZE = 200;

    private static final int DEFAULT_PREDEFINED_GEOFENCE_RADIUS = 50;
    private static final String DEFAULT_PREDEFINED_GEOFENCE_NAME = "Geofence";


    @ColorInt
    public abstract int strokeColor();

    @ColorInt
    public abstract int fillColor();

    /**
     * Minimum size defined for a Geofence, defined in Metres
     */
    public abstract int minimumGeofenceSize();

    /**
     * Maximum size defined for a Geofence, defined in Metres
     */
    public abstract int maximumGeofenceSize();

    @DrawableRes
    public abstract int centreMarkerDrawable();

    @DrawableRes
    public abstract int resizeMarkerDrawable();

    /**
     * @return Default geofence radius in meters
     */
    public abstract int geofenceCreatedRadius();

    /**
     * @return Default title a geofence is given when created, several geofences can have the same
     * title, they are uniquely distinguishably by their ID.
     */
    public abstract String geofenceCreatedName();

    public static GeofenceMapOptions.Builder create() {
        return new AutoValue_GeofenceMapOptions.Builder()
                .minimumGeofenceSize(DEFAULT_PREDEFINED_GEOFENCE_MIN_SIZE)
                .maximumGeofenceSize(DEFAULT_PREDEFINED_GEOFENCE_MAX_SIZE)
                .centreMarkerDrawable(R.drawable.ic_select_geofence)
                .resizeMarkerDrawable(R.drawable.ic_select_geofence)
                .geofenceCreatedRadius(DEFAULT_PREDEFINED_GEOFENCE_RADIUS)
                .geofenceCreatedName(DEFAULT_PREDEFINED_GEOFENCE_NAME);
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract GeofenceMapOptions.Builder strokeColor(@ColorInt int strokeColor);

        public abstract GeofenceMapOptions.Builder fillColor(@ColorInt int fillColor);

        public abstract GeofenceMapOptions.Builder geofenceCreatedRadius(int radius);

        public abstract GeofenceMapOptions.Builder geofenceCreatedName(String name);

        public abstract GeofenceMapOptions.Builder minimumGeofenceSize(int minSize);

        public abstract GeofenceMapOptions.Builder maximumGeofenceSize(int maxSize);

        public abstract GeofenceMapOptions.Builder centreMarkerDrawable(@DrawableRes int drawable);

        public abstract GeofenceMapOptions.Builder resizeMarkerDrawable(@DrawableRes int drawable);

        public abstract GeofenceMapOptions build();
    }
}
