package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;

import com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.RxSchedulersOverriderRule;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;

import static com.hgyllensvard.geofencemanager.geofence.GeofenceActionResultTestHelper.FAILED_GEOFENCE_ACTION;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceActionResultTestHelper.SUCCESS_SINGLE_GEOFENCE_ONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class AddGeofenceManagerTest {

    @Rule
    public final RxSchedulersOverriderRule rxSchedulersOverriderRule = new RxSchedulersOverriderRule();

    @Mock
    GeofenceManager geofenceManager;

    private SelectedGeofenceId selectedGeofenceId;

    private AddGeofenceManager addGeofenceManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        selectedGeofenceId = new SelectedGeofenceId();

        addGeofenceManager = new AddGeofenceManager(
                geofenceManager,
                GeofenceTestHelper.GEOFENCE_MAP_OPTIONS,
                selectedGeofenceId);
    }

    @Test
    public void attemptAddGeofence_AlreadySelectedGeofence_FailedActionReturned() throws Exception {
        selectedGeofenceId.selectedGeofence(GeofenceTestHelper.ID_ONE);

        addGeofenceManager.attemptAddGeofence(GeofenceTestHelper.LAT_LNG_ONE)
                .test()
                .assertNoErrors()
                .assertValue(actionResult -> actionResult.error() instanceof GeofenceAlreadySelectedError);
    }

    @Test
    public void attemptAddGeofence_noSelectedGeofence_failToAdd_noSelectedGeofence() {
        Mockito.when(geofenceManager.addGeofence(any(Geofence.class)))
                .thenReturn(Single.just(FAILED_GEOFENCE_ACTION));

        addGeofenceManager.attemptAddGeofence(GeofenceTestHelper.LAT_LNG_ONE)
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(actionResult -> actionResult.equals(FAILED_GEOFENCE_ACTION));

        assertThat(selectedGeofenceId.selectedGeofenceState().isGeofenceSelected()).isFalse();
    }

    @Test
    public void attemptAddGeofence_noSelectedGeofence_SuccessfullyAdd_newlySelectedGeofence() {
        Mockito.when(geofenceManager.addGeofence(any(Geofence.class)))
                .thenReturn(SUCCESS_SINGLE_GEOFENCE_ONE);

        addGeofenceManager.attemptAddGeofence(GeofenceTestHelper.LAT_LNG_ONE)
                .test()
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(actionResult -> actionResult.geofence().equals(GeofenceTestHelper.GEOFENCE_ONE));

        assertThat(selectedGeofenceId.selectedGeofenceState().geofenceId()).isEqualTo(GeofenceTestHelper.ID_ONE);
    }
}