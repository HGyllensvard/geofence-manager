package com.hgyllensvard.geofencemanager.geofence.persistence;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

@AutoValue
public abstract class GeofenceModel implements GeofenceDbModel {
    
    public static final Factory<GeofenceModel> FACTORY = new Factory<>(AutoValue_GeofenceModel::new);

    public static final RowMapper<GeofenceModel> SELECT_ALL_MAPPER = FACTORY.select_allMapper();

    public static Marshal marshal() {
        return new Marshal(null);
    }
}
