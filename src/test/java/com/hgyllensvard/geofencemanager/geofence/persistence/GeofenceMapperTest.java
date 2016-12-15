package com.hgyllensvard.geofencemanager.geofence.persistence;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GeofenceMapperTest {

    private static final String NAME = "A name";
    private static final double LATITUDE = 1d;
    private static final double LONGITUDE = 2d;

    private GeofenceMapper mapper;

    @Before
    public void setUp() {
        mapper = new GeofenceMapper();
    }

    @Test
    public void toModel() {

        LatLng latLng = new LatLng(LATITUDE, LONGITUDE);

        GeofenceModel model = mapper.toModel(NAME, latLng);

        assertThat(model.name).isEqualTo(NAME);
        assertThat(model.latitude).isEqualTo(LATITUDE);
        assertThat(model.longitude).isEqualTo(LONGITUDE);
    }

    @Test
    public void toGeofences() {
        GeofenceModel model = getGeofenceModel();

        GeofenceModel modelTwo = new GeofenceModel();

        String nameTwo = NAME + 1;
        double latitudeTwo = LATITUDE + 1;
        double longitudeTwo = LONGITUDE + 1;

        modelTwo.name = nameTwo;
        modelTwo.latitude = latitudeTwo;
        modelTwo.longitude = longitudeTwo;

        List<GeofenceModel> geofenceModels = Arrays.asList(model, modelTwo);
        List<GeofenceData> geofenceDatas = mapper.toGeofences(geofenceModels);

        for (int i = 0; i < geofenceDatas.size(); i++) {
            assertIsGeofence(geofenceDatas.get(i), geofenceModels.get(i));
        }
    }

    @Test
    public void toGeofence() {
        GeofenceModel model = getGeofenceModel();

        GeofenceData geofence = mapper.toGeofence(model);

        assertIsGeofence(geofence, NAME, LATITUDE, LONGITUDE);
    }

    private void assertIsGeofence(
            GeofenceData geofence,
            GeofenceModel model
    ) {
        assertIsGeofence(geofence,
                model.name,
                model.latitude,
                model.longitude);
    }

    private void assertIsGeofence(
            GeofenceData geofence,
            String name,
            double lat,
            double log
    ) {
        assertThat(geofence.name()).isEqualTo(name);
        assertThat(geofence.latLng().latitude).isEqualTo(lat);
        assertThat(geofence.latLng().longitude).isEqualTo(log);
    }

    private GeofenceModel getGeofenceModel() {
        GeofenceModel model = new GeofenceModel();
        model.name = NAME;
        model.latitude = LATITUDE;
        model.longitude = LONGITUDE;
        return model;
    }
}