package com.hgyllensvard.geofencemanager.geofence;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.geofence.AddGeofenceResult;
import com.hgyllensvard.geofencemanager.geofence.geofence.FailedToAddGeofenceException;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
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

import static org.mockito.Mockito.when;

public class GeofenceManagerTest {

    @Mock
    GeofenceRepository geofenceRepository;

    @Mock
    PlayServicesGeofenceManager playServicesGeofenceManager;

    GeofenceManager geofenceManager;

    private Geofence testGeofence;
    private Geofence insertedTestGeofence;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        testGeofence = Geofence.create("name", new LatLng(10, 20), 30);
        insertedTestGeofence = testGeofence.withId(1);

        geofenceManager = new GeofenceManager(geofenceRepository, playServicesGeofenceManager);
    }

    @Test
    public void shouldSuccessfullyAddGeofence() throws Exception {
        when(geofenceRepository.insert(testGeofence)).thenReturn(Single.just(insertedTestGeofence));
        when(playServicesGeofenceManager.activateGeofence(insertedTestGeofence)).thenReturn(Single.just(true));

        geofenceManager.addGeofence(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(AddGeofenceResult.success(testGeofence.withId(1)));
    }

    @Test
    public void shouldFailToInsertGeofenceIntoRepositoryWhenAttemptingToAddGeofence() {
        Exception exception = new InsertFailedException("Failz");
        when(geofenceRepository.insert(testGeofence)).thenReturn(Single.error(exception));

        geofenceManager.addGeofence(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValue(AddGeofenceResult.failure(exception));
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
    public void removeGeofence() throws Exception {

    }

    @Test
    public void updateGeofence() throws Exception {

    }

    @Test
    public void observeGeofences() throws Exception {

    }

    @Test
    public void getGeofence() throws Exception {

    }

}