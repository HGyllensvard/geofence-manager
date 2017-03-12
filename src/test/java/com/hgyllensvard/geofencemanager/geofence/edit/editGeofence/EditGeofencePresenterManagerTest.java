package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;

import com.hgyllensvard.geofencemanager.geofence.RxSchedulersOverriderRule;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.ID_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.IS_ACTIVE_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.LAT_LNG_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.LAT_LNG_TWO;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.NAME_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.NAME_TWO;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.RADIUS_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditGeofencePresenterManagerTest {

    @Rule
    public final RxSchedulersOverriderRule rxSchedulersOverriderRule = new RxSchedulersOverriderRule();

    @Mock
    GeofenceManager geofenceManager;

    private SelectedGeofence selectedGeofence;

    private EditGeofencePresenterManager editGeofencePresenterManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        selectedGeofence = new SelectedGeofence();

        editGeofencePresenterManager = new EditGeofencePresenterManager(geofenceManager, selectedGeofence);
    }

    @Test
    public void observeSelectedGeofence_ShouldReturnValidGeofence() {
        when(geofenceManager.getGeofence(ID_ONE))
                .thenReturn(Single.just(TEST_GEOFENCE_ONE_WITH_ID));

        selectedGeofence.updatedSelectedGeofence(ID_ONE);

        editGeofencePresenterManager.observeSelectedGeofence()
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(TEST_GEOFENCE_ONE_WITH_ID);
    }

    @Test
    public void observeSelectedGeofence_WithInvalidId_ShouldNotReturnAnyGeofence() {
        selectedGeofence.updatedSelectedGeofence(Geofence.NO_ID);

        editGeofencePresenterManager.observeSelectedGeofence()
                .test()
                .assertNoErrors()
                .assertNoValues();
    }

    @Test
    public void updateSelectedGeofence_InvalidGeofenceId_DoNotUpdate() {
        selectedGeofence.updatedSelectedGeofence(Geofence.NO_ID);

        editGeofencePresenterManager.updateSelectedGeofence(NAME_ONE, LAT_LNG_ONE);

        verify(geofenceManager, never()).getGeofence(any(Long.class));
    }

    @Test
    public void updateSelectedGeofence_validInput() {
        selectedGeofence.updatedSelectedGeofence(ID_ONE);
        when(geofenceManager.getGeofence(ID_ONE)).thenReturn(Single.just(TEST_GEOFENCE_ONE_WITH_ID));

        editGeofencePresenterManager.updateSelectedGeofence(NAME_TWO, LAT_LNG_TWO);

        Geofence geofence = Geofence.create(ID_ONE, NAME_TWO, LAT_LNG_TWO, RADIUS_ONE, IS_ACTIVE_ONE);
        verify(geofenceManager, times(1)).updateGeofence(geofence);
    }

    @Test
    public void deleteSelectedGeofence_NoValidGeofenceSelected_ReturnFalse() {
        selectedGeofence.updatedSelectedGeofence(Geofence.NO_ID);

        editGeofencePresenterManager.deleteSelectedGeofence()
                .test()
                .assertNoErrors()
                .assertValue(false);
    }

    @Test
    public void deleteSelectedGeofence_validGeofenceSelected_FailToDelete_ReturnFalse() {
        when(geofenceManager.removeGeofence(ID_ONE)).thenReturn(Single.just(false));

        selectedGeofence.updatedSelectedGeofence(ID_ONE);

        editGeofencePresenterManager.deleteSelectedGeofence()
                .test()
                .assertNoErrors()
                .assertValue(false);
    }

    @Test
    public void deleteSelectedGeofence_validGeofenceSelected_SucceedDelete_ReturnTrue() {
        when(geofenceManager.removeGeofence(ID_ONE)).thenReturn(Single.just(true));

        selectedGeofence.updatedSelectedGeofence(ID_ONE);

        editGeofencePresenterManager.deleteSelectedGeofence()
                .test()
                .assertNoErrors()
                .assertValue(true);

        assertThat(selectedGeofence.selectedGeofence()).isEqualTo(Geofence.NO_ID);
    }
}