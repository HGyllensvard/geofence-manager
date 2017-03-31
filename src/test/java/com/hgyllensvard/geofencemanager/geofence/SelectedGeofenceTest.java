package com.hgyllensvard.geofencemanager.geofence;

import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.ID_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectedGeofenceTest {

    @Mock
    GeofenceManager geofenceManager;

    private SelectedGeofenceId selectedGeofenceId;

    private SelectedGeofence selectedGeofence;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        selectedGeofenceId = new SelectedGeofenceId();

        selectedGeofence = new SelectedGeofence(selectedGeofenceId, geofenceManager);
    }

    @Test
    public void selectedGeofence_noSelectedGeofenceId_dummyGeofenceReturned() {
        mockNoGeofence();

        selectedGeofence.selectedGeofence()
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(Geofence.sDummyGeofence);

        verify(geofenceManager, never()).getGeofence(any(Long.class));
    }

    @Test
    public void selectedGeofence_selectedGeofence_noGeofenceInDatabase_dummyGeofenceReturned() {
        mockNoGeofence();
        selectGeofenceIdOne();

        selectedGeofence.selectedGeofence()
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(Geofence.sDummyGeofence);

        verify(geofenceManager, times(1)).getGeofence(ID_ONE);
    }

    @Test
    public void selectedGeofence_selected_geofenceInDatabase_returnGeofence() {
        mockGeofenceOne();
        selectGeofenceIdOne();

        selectedGeofence.selectedGeofence()
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(TEST_GEOFENCE_ONE_WITH_ID);
    }

    @Test
    public void selectedValidGeofence_noSelection_noValueReturned() {
        mockNoGeofence();

        selectedGeofence.selectedValidGeofence()
                .test()
                .assertNoErrors()
                .assertNoValues();
    }

    @Test
    public void selectedValidGeofence_geofenceSelected_geofenceReturned() {
        mockGeofenceOne();
        selectGeofenceIdOne();

        selectedGeofence.selectedValidGeofence()
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(TEST_GEOFENCE_ONE_WITH_ID);
    }

    @Test
    public void observeValidSelectedGeofence() {
        mockGeofenceOne();
        selectGeofenceIdOne();

        selectedGeofence.observeValidSelectedGeofence()
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(TEST_GEOFENCE_ONE_WITH_ID);
    }

    @Test
    public void observeSelectedGeofence_shouldReturnValidGeofence() {
        mockGeofenceOne();
        selectGeofenceIdOne();

        selectedGeofence.observeSelectedGeofence()
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(TEST_GEOFENCE_ONE_WITH_ID);
    }

    @Test
    public void observeSelectedGeofence_withInvalidId_shouldNotReturnAnyGeofence() {
        selectedGeofence.observeValidSelectedGeofence()
                .test()
                .assertNoErrors()
                .assertNoValues();
    }

    @Test
    public void deleteSelectedGeofence_noValidGeofenceSelected_returnFalse() {
        selectedGeofence.delete()
                .test()
                .assertNoErrors()
                .assertValue(false);
    }

    @Test
    public void deleteSelectedGeofence_validGeofenceSelected_failToDelete_returnFalse() {
        when(geofenceManager.removeGeofence(ID_ONE)).thenReturn(Single.just(false));

        selectGeofenceIdOne();

        selectedGeofence.delete()
                .test()
                .assertNoErrors()
                .assertValue(false);
    }

    @Test
    public void deleteSelectedGeofence_validGeofenceSelected_succeedDelete_returnTrue() {
        when(geofenceManager.removeGeofence(ID_ONE)).thenReturn(Single.just(true));

        selectGeofenceIdOne();

        selectedGeofence.delete()
                .test()
                .assertNoErrors()
                .assertValue(true);

        assertThat(selectedGeofenceId.selectedGeofenceId()).isEqualTo(Geofence.NO_ID);
    }

    private void selectGeofenceIdOne() {
        selectedGeofenceId.selectedGeofence(ID_ONE);
    }

    private void mockGeofenceOne() {
        when(geofenceManager.getGeofence(ID_ONE)).thenReturn(Single.just(TEST_GEOFENCE_ONE_WITH_ID));
    }

    private void mockNoGeofence() {
        when(geofenceManager.getGeofence(any(Long.class))).thenReturn(Single.just(Geofence.sDummyGeofence));
    }
}