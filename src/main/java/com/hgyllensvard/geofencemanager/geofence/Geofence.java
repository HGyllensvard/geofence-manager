package com.hgyllensvard.geofencemanager.geofence;

import com.google.android.gms.maps.model.LatLng;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Geofence {

    public static final long NO_ID = -1;

    public abstract long id();

    public abstract String name();

    public abstract LatLng latLng();

    public abstract float radius();

    public static Geofence create(
            long id,
            String name,
            LatLng latLng,
            float radius
    ) {
        return new AutoValue_Geofence.Builder()
                .id(id)
                .name(name)
                .latLng(latLng)
                .radius(radius)
                .build();
    }

    public static Geofence create(
            String name,
            LatLng latLng,
            float radius
    ) {
        return create(NO_ID,
                name,
                latLng,
                radius);
    }

    abstract Builder toBuilder();

    public Geofence withId(long id) {
        return toBuilder().id(id).build();
    }

    public Geofence withName(String name) {
        return toBuilder().name(name).build();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder id(long id);

        abstract Builder name(String name);

        abstract Builder latLng(LatLng latLng);

        abstract Builder radius(float radius);

        abstract Geofence build();
    }
}
