package com.hgyllensvard.geofencemanager.geofence.geofence;

import com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.persistence.exceptions.InsertFailedException;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

import static org.mockito.Mockito.when;

public class GeofenceManagerTest {

    @Mock
    GeofenceRepository geofenceRepository;

    @Mock
    PlayServicesGeofenceManager playServicesGeofenceManager;

    private GeofenceManager geofenceManager;

    private Geofence testGeofence = GeofenceTestHelper.TEST_GEOFENCE_ONE;
    private Geofence insertedTestGeofence = GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        geofenceManager = new GeofenceManager(geofenceRepository, playServicesGeofenceManager);
    }

    @Test
    public void successfullyAddGeofence() {
        when(geofenceRepository.insert(testGeofence)).thenReturn(Single.just(insertedTestGeofence));
        when(playServicesGeofenceManager.activateGeofence(insertedTestGeofence)).thenReturn(Single.just(true));

        geofenceManager.addGeofence(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(GeofenceActionResult.success(testGeofence.withId(1)));
    }

    @Test
    public void failToInsertGeofenceIntoRepositoryWhenAttemptingToAddGeofence() {
        when(geofenceRepository.insert(testGeofence)).thenReturn(Single.just(Geofence.sDummyGeofence));

        geofenceManager.addGeofence(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(geofenceActionResult -> geofenceActionResult.error() instanceof InsertFailedException);
    }

    @Test
    public void failToAddGeofenceAfterSavedToDatabase_attemptToCleanUpDatabaseAfterwards() {
        when(geofenceRepository.insert(testGeofence)).thenReturn(Single.just(insertedTestGeofence));
        when(playServicesGeofenceManager.activateGeofence(insertedTestGeofence)).thenReturn(Single.just(false));
        when(geofenceRepository.delete(1)).thenReturn(Single.just(true));

        geofenceManager.addGeofence(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(addGeofenceResult -> addGeofenceResult.error() instanceof FailedToAddGeofenceException);
    }

    @Test
    public void successfullyRemoveGeofence() {
        when(playServicesGeofenceManager.removeGeofence(insertedTestGeofence.id())).thenReturn(Single.just(false));
        when(geofenceRepository.delete(1)).thenReturn(Single.just(true));

        geofenceManager.removeGeofence(insertedTestGeofence.id())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(true);
    }

    @Test
    public void succeedCheckGeofenceExist() {
        when(geofenceRepository.getGeofence(GeofenceTestHelper.ID_ONE)).thenReturn(Single.just(testGeofence));

        geofenceManager.exist(GeofenceTestHelper.ID_ONE)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(true);
    }

    @Test
    public void succeedCheckGeofenceDoesNotExist() {
        when(geofenceRepository.getGeofence(GeofenceTestHelper.ID_ONE)).thenReturn(Single.just(Geofence.sDummyGeofence));

        geofenceManager.exist(GeofenceTestHelper.ID_ONE)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(false);
    }

    @Test
    public void returnFalseIfErrorOccursWhenDeletingGeofence() {
        when(playServicesGeofenceManager.removeGeofence(insertedTestGeofence.id())).thenReturn(Single.error(new RuntimeException("Cool")));

        geofenceManager.removeGeofence(insertedTestGeofence.id())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(false);
    }

    @Test
    public void successfullyUpdateGeofence() {
        when(playServicesGeofenceManager.removeGeofence(1)).thenReturn(Single.just(true));
        when(geofenceRepository.update(insertedTestGeofence)).thenReturn(Single.just(true));
        when(playServicesGeofenceManager.activateGeofence(insertedTestGeofence)).thenReturn(Single.just(true));

        geofenceManager.updateGeofence(insertedTestGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue(value -> value.equals(GeofenceActionResult.success(insertedTestGeofence)));
    }
}