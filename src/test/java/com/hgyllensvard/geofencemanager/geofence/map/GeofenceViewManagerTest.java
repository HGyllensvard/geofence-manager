package com.hgyllensvard.geofencemanager.geofence.map;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.GeofenceMapOptions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class GeofenceViewManagerTest {

    @Mock
    GeofenceMapOptions geofenceMapOptions;

    private GeofenceViewManager geofenceViewManager;

    private static final long GEOFENCE_ID = 2;
    private static final String NAME = "NAME";
    private static final LatLng LAT_LNG = new LatLng(10, 20);
    private static final float RADIUS = 5;

    private final Geofence testGeofence = Geofence.create(GEOFENCE_ID, NAME, LAT_LNG, RADIUS);
    private final List<Geofence> testGeofences = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testGeofences.add(testGeofence);

        geofenceViewManager = new GeofenceViewManager(geofenceMapOptions);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createMarkers() throws Exception {

    }

    @Test
    public void findGeofenceId() throws Exception {

    }

    @Test
    public void deleteMarker() throws Exception {

    }

}