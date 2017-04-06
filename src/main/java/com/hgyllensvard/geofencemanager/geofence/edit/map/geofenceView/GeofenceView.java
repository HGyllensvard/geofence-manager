package com.hgyllensvard.geofencemanager.geofence.edit.map.geofenceView;

import android.support.annotation.ColorInt;

import com.google.android.gms.maps.model.LatLng;
import com.google.auto.value.AutoValue;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

@AutoValue
public abstract class GeofenceView {

    public abstract Geofence geofence();

    public abstract int fillColor();

    public abstract int strokeColor();

    public long id() {
        return geofence().id();
    }

    public LatLng latLng() {
        return geofence().latLng();
    }

    public float radius() {
        return geofence().radius();
    }

    public static GeofenceView create(Geofence geofence,
                                      @ColorInt int fillColor,
                                      @ColorInt int strokeColor) {
        return new AutoValue_GeofenceView(geofence, fillColor, strokeColor);
    }
}
