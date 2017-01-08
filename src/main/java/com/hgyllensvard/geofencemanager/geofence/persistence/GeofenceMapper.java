package com.hgyllensvard.geofencemanager.geofence.persistence;


import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

import java.util.ArrayList;
import java.util.List;

class GeofenceMapper {

    List<GeofenceData> toGeofences(List<GeofenceModel> geofenceModelOlds) {
        List<GeofenceData> geofenceDatas = new ArrayList<>();

        for (GeofenceModel geofenceModelOld : geofenceModelOlds) {
            geofenceDatas.add(toGeofence(geofenceModelOld));
        }

        return geofenceDatas;
    }

    GeofenceData toGeofence(GeofenceModel geofenceModel) {
        return GeofenceData.create(
                geofenceModel.name(),
                new LatLng(geofenceModel.latitude(), geofenceModel.longitude()),
                (float) geofenceModel.radius());
    }

    ContentValues toContentValues(@NonNull GeofenceData download) {
        final GeofenceModel.Marshal marshal = GeofenceModel.marshal()
                .name(download.name())
                .latitude(download.latLng().latitude)
                .longitude(download.latLng().longitude)
                .radius(download.radius());

        if (download.id() != GeofenceData.NO_ID) {
            marshal._id(download.id());
        }

        return marshal.asContentValues();
    }
}
