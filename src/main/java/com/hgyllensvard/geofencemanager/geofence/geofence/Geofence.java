package com.hgyllensvard.geofencemanager.geofence.geofence;

import com.google.android.gms.maps.model.LatLng;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Geofence {

    public static Geofence sDummyGeofence = Geofence.create("", new LatLng(-1, -1), -1, false);

    public static final long NO_ID = -1;

    public abstract long id();

    public abstract String name();

    public abstract LatLng latLng();

    public abstract float radius();

    public abstract boolean active();

    public static Geofence create(
            long id,
            String name,
            LatLng latLng,
            float radius,
            boolean active
    ) {
        return new AutoValue_Geofence.Builder()
                .id(id)
                .name(name)
                .latLng(latLng)
                .radius(radius)
                .active(active)
                .build();
    }

    public static Geofence create(
            String name,
            LatLng latLng,
            float radius,
            boolean active
    ) {
        return create(NO_ID,
                name,
                latLng,
                radius,
                active);
    }

    abstract Builder toBuilder();

    public Geofence withId(long id) {
        return toBuilder().id(id).build();
    }

    public Geofence withName(String name) {
        return toBuilder().name(name).build();
    }

    public Geofence withLatLng(LatLng latLng) {
        return toBuilder().latLng(latLng).build();
    }

    public Geofence withRadius(float radius) {
        return toBuilder().radius(radius).build();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder id(long id);

        abstract Builder name(String name);

        abstract Builder latLng(LatLng latLng);

        abstract Builder radius(float radius);

        abstract Builder active(boolean active);

        abstract Geofence build();
    }
}
