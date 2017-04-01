package com.hgyllensvard.geofencemanager.geofence;

import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceIdState;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;

import static com.hgyllensvard.geofencemanager.geofence.geofence.Geofence.NO_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class SelectedGeofenceIdTest {

    private static final SelectedGeofenceIdState NO_SELECTION = SelectedGeofenceIdState.noSelection();
    private static final SelectedGeofenceIdState ID_ONE = SelectedGeofenceIdState.selected(GeofenceTestHelper.ID_ONE);
    private static final SelectedGeofenceIdState ID_TWO = SelectedGeofenceIdState.selected(GeofenceTestHelper.ID_TWO);

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

        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(ID_TWO);
        assertUpdateIdTo(NO_SELECTION);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(2)
                .assertValues(GeofenceTestHelper.ID_ONE, GeofenceTestHelper.ID_TWO);
    }

    @Test
    public void observeSelectedGeofence_updateSelectionMultipleTimes() {
        TestObserver<SelectedGeofenceIdState> selectedGeofence = selectedGeofenceId.observeSelectedGeofenceId().test();

        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(ID_TWO);
        assertUpdateIdTo(NO_SELECTION);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(4)
                .assertValues(NO_SELECTION, ID_ONE, ID_TWO, NO_SELECTION);
    }

    @Test
    public void observeSelectedGeofence_doNotSendUpdatesForSameSelection() {
        TestObserver<SelectedGeofenceIdState> selectedGeofence = selectedGeofenceId.observeSelectedGeofenceId().test();

        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(ID_ONE);
        assertUpdateIdTo(ID_ONE);

        selectedGeofence
                .assertNoErrors()
                .assertValueCount(2)
                .assertValues(NO_SELECTION, ID_ONE);
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
        assertThat(selectedGeofenceId.selectedGeofenceState()).isEqualTo(NO_SELECTION);
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
        assertUpdateIdTo(NO_SELECTION);
    }

    private void assertUpdateIdTo(SelectedGeofenceIdState geofenceState) {
        assertThat(selectedGeofenceId.selectedGeofence(geofenceState.geofenceId())).isTrue();
        assertThat(selectedGeofenceId.selectedGeofenceId()).isEqualTo(geofenceState.geofenceId());
    }
}