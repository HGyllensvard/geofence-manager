package com.hgyllensvard.geofencemanager.geofence.persistence;


import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;

import java.util.ArrayList;
import java.util.List;

class GeofenceMapper {

    List<Geofence> toGeofences(List<GeofenceModel> geofenceModelOlds) {
        List<Geofence> geofences = new ArrayList<>();

        for (GeofenceModel geofenceModelOld : geofenceModelOlds) {
            geofences.add(toGeofence(geofenceModelOld));
        }

        return geofences;
    }

    Geofence toGeofence(GeofenceModel geofenceModel) {
        return Geofence.create(
                geofenceModel._id(),
                geofenceModel.name(),
                new LatLng(geofenceModel.latitude(), geofenceModel.longitude()),
                (float) geofenceModel.radius());
    }

    ContentValues toContentValues(@NonNull Geofence download) {
        final GeofenceModel.Marshal marshal = GeofenceModel.marshal()
                .name(download.name())
                .latitude(download.latLng().latitude)
                .longitude(download.latLng().longitude)
                .radius(download.radius());

        if (download.id() != Geofence.NO_ID) {
            marshal._id(download.id());
        }

        return marshal.asContentValues();
    }
}
