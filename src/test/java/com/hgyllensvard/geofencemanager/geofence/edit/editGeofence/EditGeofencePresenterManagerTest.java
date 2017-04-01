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

import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.ID_ONE;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.IS_ACTIVE_ONE;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.LAT_LNG_TWO;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.NAME_TWO;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.RADIUS_ONE;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.GEOFENCE_ONE;
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
        when(selectedGeofence.selectedValidGeofence()).thenReturn(Maybe.just(GEOFENCE_ONE));

        editGeofencePresenterManager.updateSelectedGeofence(NAME_TWO, LAT_LNG_TWO);

        Geofence geofence = Geofence.create(ID_ONE, NAME_TWO, LAT_LNG_TWO, RADIUS_ONE, IS_ACTIVE_ONE);
        verify(geofenceManager, times(1)).updateGeofence(geofence);
    }
}