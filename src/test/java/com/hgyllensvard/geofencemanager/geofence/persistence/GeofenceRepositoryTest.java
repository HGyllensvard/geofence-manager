package com.hgyllensvard.geofencemanager.geofence.persistence;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.ContextModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.TestApplication;
import com.hgyllensvard.geofencemanager.geofence.persistence.exceptions.NoSuchGeofenceExistException;
import com.squareup.sqlbrite.BriteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class, manifest = Config.NONE)
public class GeofenceRepositoryTest {

    @Singleton
    @Component(
            modules = {ContextModule.class, GeofencePersistenceModule.class}
    )
    public interface TestRepositoryComponent {
        void inject(GeofenceRepositoryTest testClass);
    }

    @Inject
    GeofenceRepository repository;

    @Inject
    BriteDatabase database;

    private static final String NAME = "Name";
    private static final long LATITUDE = 10;
    private static final long LONGITUDE = 12;
    private static final long RADIUS = 1;

    private Geofence testGeofence;

    @Before
    public void setUp() {
        TestRepositoryComponent testRepositoryComponent = DaggerGeofenceRepositoryTest_TestRepositoryComponent.builder()
                .contextModule(new ContextModule(RuntimeEnvironment.application))
                .geofencePersistenceModule(new GeofencePersistenceModule())
                .build();

        testRepositoryComponent.inject(this);

        clearDatabase();

        testGeofence = Geofence.create(NAME, new LatLng(LATITUDE, LONGITUDE), RADIUS, true);
    }

    @After
    public void tearDown() {
        clearDatabase();
    }

    @Test
    public void shouldInsertGeofence() {
        repository.insert(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValues(testGeofence.withId(1));
    }

    @Test
    public void  shouldDeleteGeofence() {
        Geofence insertedGeofence = repository.insert(testGeofence)
                .blockingGet();

        assertThat(insertedGeofence).isNotNull();
        repository.delete(insertedGeofence.id())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue(true);
    }

    @Test
    public void shouldNotDeleteGeofenceWithNoId() {
        repository.delete(testGeofence.id())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValues(false);
    }

    @Test
    public void shouldNotDeleteGeofenceTwice() {
        Geofence result = repository.insert(testGeofence)
                .blockingGet();

        assertThat(result).isNotNull();
        repository.delete(result.id())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValues(true);

        repository.delete(result.id())
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValue(false);
    }

    @Test
    public void shouldFetchGeofenceWithId() {
        Geofence geofence = repository.insert(testGeofence)
                .blockingGet();

        Geofence fetchedGeofence = repository.getGeofence(geofence.id())
                .blockingGet();

        assertThat(fetchedGeofence).isEqualTo(geofence);
    }

    @Test
    public void shouldThrowErrorIfCantFetchGeofence() {
        repository.getGeofence(1)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertError(NoSuchGeofenceExistException.class);
    }

    @Test
    public void shouldUpdateGeofenceData() {
        Geofence geofence = repository.insert(testGeofence)
                .blockingGet();

        String newName = "Some newName";
        repository.update(geofence.withName(newName))
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValues(true);

        repository.listenGeofences()
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValueCount(1)
                .assertValueAt(0, testGeofences -> testGeofences.size() == 1 &&
                        testGeofences.get(0).equals(geofence.withName(newName)));
    }

    @Test
    public void shouldNotUpdateNonExistingGeofence() {
        repository.update(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValues(false);
    }

    @Test
    public void shouldFetchKnownGeofencesWhenSubscribing() {
        repository.insert(testGeofence)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValues(testGeofence.withId(1));

        repository.listenGeofences()
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValueCount(1)
                .assertValueAt(0, testGeofences -> testGeofences.size() == 1 &&
                        testGeofences.get(0).equals(testGeofence.withId(1)));

        Geofence geofenceTwo = Geofence.create("New Geofence", new LatLng(2, 2), 2, true);
        repository.insert(geofenceTwo)
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValues(geofenceTwo.withId(2));

        repository.listenGeofences()
                .test()
                .awaitDone(1, TimeUnit.SECONDS)
                .assertValueCount(1)
                .assertValueAt(0, testGeofences ->
                        testGeofences.size() == 2 &&
                        testGeofences.get(1).equals(geofenceTwo.withId(2)));
    }

    private void clearDatabase() {
        database.delete(GeofenceModel.TABLE_NAME, null);
    }
}