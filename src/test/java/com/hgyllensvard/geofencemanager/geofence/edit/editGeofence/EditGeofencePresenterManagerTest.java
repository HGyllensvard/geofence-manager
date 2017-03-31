package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;

import com.hgyllensvard.geofencemanager.RxSchedulersOverriderRule;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Maybe;

import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.ID_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.IS_ACTIVE_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.LAT_LNG_TWO;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.NAME_TWO;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.RADIUS_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditGeofencePresenterManagerTest {

    @Rule
    public final RxSchedulersOverriderRule rxSchedulersOverriderRule = new RxSchedulersOverriderRule();

    @Mock
    GeofenceManager geofenceManager;

    @Mock
    SelectedGeofence selectedGeofence;

    private SelectedGeofenceId selectedGeofenceId;

    private EditGeofencePresenterManager editGeofencePresenterManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        selectedGeofenceId = new SelectedGeofenceId();

        editGeofencePresenterManager = new EditGeofencePresenterManager(geofenceManager, selectedGeofence);
    }

    @Test
    public void updateSelectedGeofence_validInput() {
        selectedGeofenceId.selectedGeofence(ID_ONE);
        when(selectedGeofence.selectedValidGeofence()).thenReturn(Maybe.just(TEST_GEOFENCE_ONE_WITH_ID));

        editGeofencePresenterManager.updateSelectedGeofence(NAME_TWO, LAT_LNG_TWO);

        Geofence geofence = Geofence.create(ID_ONE, NAME_TWO, LAT_LNG_TWO, RADIUS_ONE, IS_ACTIVE_ONE);
        verify(geofenceManager, times(1)).updateGeofence(geofence);
    }
}