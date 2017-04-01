package com.hgyllensvard.geofencemanager.geofence.selectedGeofence;

import com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceIdTestHelper;
import com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceIdState;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static com.hgyllensvard.geofencemanager.geofence.geofence.Geofence.NO_ID;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceIdTestHelper.ID_ONE_STATE;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceIdTestHelper.ID_TWO_STATE;
import static com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceIdState.NO_ID_SELECTED;
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
    public void selectedGeofenceState_defaultNoId() {
        assertThat(selectedGeofenceId.selectedGeofenceState().geofenceId()).isEqualTo(NO_ID);
        assertThat(selectedGeofenceId.selectedGeofenceState().isGeofenceSelected()).isFalse();
    }

    @Test
    public void observeValidSelectedGeofence_updateSelectionMultipleTimes() {
        TestObserver<Long> selectedGeofence = selectedGeofenceId.observeValidSelectedGeofenceId().test();

        assertUpdateIdTo(ID_ONE_STATE);
        assertUpdateIdTo(ID_TWO_STATE);
        assertUpdateIdTo(NO_ID_SELECTED);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(2)
                .assertValues(GeofenceTestHelper.ID_ONE, GeofenceTestHelper.ID_TWO);
    }

    @Test
    public void observeSelectedGeofence_updateSelectionMultipleTimes() {
        TestObserver<SelectedGeofenceIdState> selectedGeofence = selectedGeofenceId.observeSelectedGeofenceId().test();

        assertUpdateIdTo(ID_ONE_STATE);
        assertUpdateIdTo(ID_TWO_STATE);
        assertUpdateIdTo(NO_ID_SELECTED);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(4)
                .assertValues(NO_ID_SELECTED, ID_ONE_STATE, ID_TWO_STATE, NO_ID_SELECTED);
    }

    @Test
    public void observeSelectedGeofence_doNotSendUpdatesForSameSelection() {
        TestObserver<SelectedGeofenceIdState> selectedGeofence = selectedGeofenceId.observeSelectedGeofenceId().test();

        assertUpdateIdTo(ID_ONE_STATE);
        assertUpdateIdTo(ID_ONE_STATE);
        assertUpdateIdTo(ID_ONE_STATE);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(2)
                .assertValues(NO_ID_SELECTED, ID_ONE_STATE);
    }

    @Test
    public void isGeofenceSelected_noSelection_returnFalse() {
        assertThat(selectedGeofenceId.isGeofenceSelected()).isFalse();
    }

    @Test
    public void isGeofenceSelected_geofenceSelected_returnTrue() {
        assertUpdateIdTo(ID_ONE_STATE);
        assertThat(selectedGeofenceId.isGeofenceSelected()).isTrue();
    }

    @Test
    public void setNoSelection_checkNoIdSelected() {
        assertUpdateIdTo(ID_ONE_STATE);
        selectedGeofenceId.setNoSelection();
        assertThat(selectedGeofenceId.selectedGeofenceState()).isEqualTo(NO_ID_SELECTED);
    }

    @Test
    public void updatedSelectedGeofence_invalidGeofence_getException() {
        assertThat(selectedGeofenceId.selectedGeofence(INVALID_GEOFENCE_ID)).isFalse();
    }

    @Test
    public void updatedSelectedGeofence_updateId_newIdSet() {
        assertUpdateIdTo(ID_ONE_STATE);
    }

    @Test
    public void updatedSelectedGeofence_checkCanReturnSelectionToNoSelectedId() {
        assertUpdateIdTo(ID_ONE_STATE);
        assertUpdateIdTo(NO_ID_SELECTED);
    }

    private void assertUpdateIdTo(SelectedGeofenceIdState geofenceState) {
        assertThat(selectedGeofenceId.selectedGeofence(geofenceState.geofenceId())).isTrue();
        assertThat(selectedGeofenceId.selectedGeofenceId()).isEqualTo(geofenceState.geofenceId());
    }
}