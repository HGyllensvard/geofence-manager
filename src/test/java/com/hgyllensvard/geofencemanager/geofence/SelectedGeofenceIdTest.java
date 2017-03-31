package com.hgyllensvard.geofencemanager.geofence;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.ID_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.ID_TWO;
import static com.hgyllensvard.geofencemanager.geofence.geofence.Geofence.NO_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class SelectedGeofenceIdTest {

    private static final int INVALID_GEOFENCE_ID = -1000;

    private SelectedGeofenceId selectedGeofenceId;

    @Before
    public void setUp() {
        selectedGeofenceId = new SelectedGeofenceId();
    }

    @Test
    public void selectedGeofenceId_defaultNoId() {
        assertThat(selectedGeofenceId.selectedGeofenceId()).isEqualTo(NO_ID);
    }

    @Test
    public void observeValidSelectedGeofence_updateSelectionMultipleTimes() {
        TestObserver<Long> selectedGeofence = selectedGeofenceId.observeValidSelectedGeofenceId().test();

        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(ID_TWO);
        assertUpdateIdTo(NO_ID);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(2)
                .assertValues(ID_ONE, ID_TWO);
    }

    @Test
    public void observeSelectedGeofence_updateSelectionMultipleTimes() {
        TestObserver<Long> selectedGeofence = selectedGeofenceId.observeSelectedGeofenceId().test();

        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(ID_TWO);
        assertUpdateIdTo(NO_ID);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(4)
                .assertValues(NO_ID, ID_ONE, ID_TWO, NO_ID);
    }

    @Test
    public void observeSelectedGeofence_doNotSendUpdatesForSameSelection() {
        TestObserver<Long> selectedGeofence = selectedGeofenceId.observeSelectedGeofenceId().test();

        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(ID_ONE);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(2)
                .assertValues(NO_ID, ID_ONE);
    }

    @Test
    public void isGeofenceSelected_noSelection_returnFalse() {
        assertThat(selectedGeofenceId.isGeofenceSelected()).isFalse();
    }

    @Test
    public void isGeofenceSelected_geofenceSelected_returnTrue() {
        assertUpdateIdTo(ID_ONE);
        assertThat(selectedGeofenceId.isGeofenceSelected()).isTrue();
    }

    @Test
    public void setNoSelection_checkNoIdSelected() {
        assertUpdateIdTo(ID_ONE);
        selectedGeofenceId.setNoSelection();
        assertThat(selectedGeofenceId.selectedGeofenceId()).isEqualTo(NO_ID);
    }

    @Test
    public void updatedSelectedGeofence_invalidGeofence_getException() {
        assertThat(selectedGeofenceId.selectedGeofence(INVALID_GEOFENCE_ID)).isFalse();
    }

    @Test
    public void updatedSelectedGeofence_updateId_newIdSet() {
        assertUpdateIdTo(ID_ONE);
    }

    @Test
    public void updatedSelectedGeofence_checkCanReturnSelectionToNoSelectedId() {
        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(NO_ID);
    }

    private void assertUpdateIdTo(long geofenceId) {
        assertThat(selectedGeofenceId.selectedGeofence(geofenceId)).isTrue();
        assertThat(selectedGeofenceId.selectedGeofenceId()).isEqualTo(geofenceId);
    }
}