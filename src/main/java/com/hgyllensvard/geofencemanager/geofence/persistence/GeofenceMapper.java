package com.hgyllensvard.geofencemanager.geofence.persistence;


import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

import java.util.ArrayList;
import java.util.List;

class GeofenceMapper {

    GeofenceModel toModel(String name, LatLng latLng, int radius) {
        GeofenceModel geofenceModel = new GeofenceModel();
        geofenceModel.name = name;
        geofenceModel.latitude = latLng.latitude;
        geofenceModel.longitude = latLng.longitude;
        geofenceModel.radius = radius;

        return geofenceModel;
    }

    List<GeofenceData> toGeofences(List<GeofenceModel> geofenceModels) {
        List<GeofenceData> geofenceDatas = new ArrayList<>();

        for (GeofenceModel geofenceModel : geofenceModels) {
            geofenceDatas.add(toGeofence(geofenceModel));
        }

        return geofenceDatas;
    }

    GeofenceData toGeofence(GeofenceModel geofenceModel) {
        return GeofenceData.create(
                geofenceModel.name,
                new LatLng(geofenceModel.latitude, geofenceModel.longitude),
                geofenceModel.radius);
    }
}
