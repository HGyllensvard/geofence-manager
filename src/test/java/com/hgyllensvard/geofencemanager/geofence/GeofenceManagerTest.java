package com.hgyllensvard.geofencemanager.geofence;

import com.hgyllensvard.geofencemanager.geofence.geofence.FailedToAddGeofenceException;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceActionResult;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.persistence.GeofenceRepository;
import com.hgyllensvard.geofencemanager.geofence.persistence.exceptions.InsertFailedException;
import com.hgyllensvard.geofencemanager.geofence.playIntegration.PlayServicesGeofenceManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GeofenceManagerTest {

    @Mock
    GeofenceRepository geofenceRepository;

    @Mock
    PlayServicesGeofenceManager playServicesGeofenceManager;

    private GeofenceManager geofenceManager;

    private Geofence testGeofence = GeofenceTestHelper.testGeofence;
    private Geofence insertedTestGeofence = GeofenceTestHelper.insertedTestGeofence;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        geofenceManager = new GeofenceManager(geofenceRepository, playServicesGeofenceManager);
    }

    @Test
    public void shouldSuccessfullyAddGeofence() {
        when(geofenceRepository.insert(testGeofence)).thenReturn(Single.just(insertedTestGeofence));
        when(playServicesGeofenceManager.activateGeofence(insertedTestGeofence)).thenReturn(Single.just(true));

        geofenceManager.addGeofence(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(GeofenceActionResult.success(testGeofence.withId(1)));
    }

    @Test
    public void shouldFailToInsertGeofenceIntoRepositoryWhenAttemptingToAddGeofence() {
        Exception exception = new InsertFailedException("Failz");
        when(geofenceRepository.insert(testGeofence)).thenReturn(Single.error(exception));

        geofenceManager.addGeofence(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(GeofenceActionResult.failure(exception));
    }

    @Test
    public void shouldFailToAddGeofenceAfterSavedToDatabase_shouldAttemptToCleanUpDatabaseAfterwards() {
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
    public void shouldSuccessfullyRemoveGeofence() {
        when(playServicesGeofenceManager.removeGeofence(insertedTestGeofence.id())).thenReturn(Single.just(false));
        when(geofenceRepository.delete(1)).thenReturn(Single.just(true));

        geofenceManager.removeGeofence(insertedTestGeofence.id())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(true);
    }

    @Test
    public void shouldReturnFalseIfErrorOccursWhenDeletingGeofence() {
        when(playServicesGeofenceManager.removeGeofence(insertedTestGeofence.id())).thenReturn(Single.error(new RuntimeException("Cool")));

        geofenceManager.removeGeofence(insertedTestGeofence.id())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(false);
    }

    @Test
    public void shouldSuccessfullyUpdateGeofence() {
        when(playServicesGeofenceManager.removeGeofence(1)).thenReturn(Single.just(true));
        when(geofenceRepository.update(insertedTestGeofence)).thenReturn(Single.just(true));
        when(playServicesGeofenceManager.activateGeofence(insertedTestGeofence)).thenReturn(Single.just(true));

        geofenceManager.updateGeofence(insertedTestGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue(value -> {
                    return value.equals(GeofenceActionResult.success(insertedTestGeofence));
                });
    }

    @Test
    public void observeGeofences() {
        verify(geofenceRepository, times(1)).listenGeofences();
    }

    @Test
    public void getGeofence() {
        verify(geofenceRepository, times(1)).getGeofence(any(Long.class));
    }
}