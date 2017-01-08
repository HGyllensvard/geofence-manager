package com.hgyllensvard.geofencemanager.geofence;

import com.google.android.gms.maps.model.LatLng;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GeofenceData {

    public static final long NO_ID = -1;

    public abstract long id();

    public abstract String name();

    public abstract LatLng latLng();

    public abstract float radius();

    public static GeofenceData create(
            String name,
            long latitude,
            long longitude,
            float radius
    ) {
        return create(name,
                new LatLng(latitude, longitude),
                radius);
    }

    public static GeofenceData create(
            String name,
            LatLng latLng,
            float radius
    ) {
        return new AutoValue_GeofenceData.Builder()
                .id(NO_ID)
                .name(name)
                .latLng(latLng)
                .radius(radius)
                .build();
    }

    abstract Builder toBuilder();

    public GeofenceData withId(long id) {
        return toBuilder().id(id).build();
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder id(long id);

        abstract Builder name(String name);

        abstract Builder latLng(LatLng latLng);

        abstract Builder radius(float radius);

        abstract GeofenceData build();
    }
}
