package com.hgyllensvard.geofencemanager.geofence.persistence;

import com.hgyllensvard.geofencemanager.buildingBlocks.di.ContextModule;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.PerActivity;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;
import com.hgyllensvard.geofencemanager.geofence.TestApplication;
import com.squareup.sqlbrite.BriteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import dagger.Component;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class, manifest = Config.NONE)
public class GeofenceRepositoryTest {

    @PerActivity
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

    private GeofenceData testGeofence;

    @Before
    public void setUp() throws Exception {
        TestRepositoryComponent testRepositoryComponent = DaggerGeofenceRepositoryTest_TestRepositoryComponent.builder()
                .contextModule(new ContextModule(RuntimeEnvironment.application))
                .geofencePersistenceModule(new GeofencePersistenceModule())
                .build();

        testRepositoryComponent.inject(this);

        clearDatabase();

        testGeofence = GeofenceData.create(NAME, LATITUDE, LONGITUDE, RADIUS);
    }

    @After
    public void tearDown() throws Exception {
        clearDatabase();
    }

    @Test
    public void shouldInsertGeofence() {
        repository.insert(testGeofence)
                .test()
                .assertValues(testGeofence.withId(1));
    }

    @Test
    public void shouldDeleteGeofence() {
        GeofenceData insertedGeofence = repository.insert(testGeofence)
                .blockingGet();

        assertThat(insertedGeofence).isNotNull();
        repository.delete(insertedGeofence)
                .test()
                .assertValue(true);
    }

    @Test
    public void shouldNotDeleteGeofenceWithNoId() {
        repository.delete(testGeofence)
                .test()
                .assertValues(false);
    }

    @Test
    public void shouldNotDeleteGeofenceTwice() {
        GeofenceData result = repository.insert(testGeofence)
                .blockingGet();

        assertThat(result).isNotNull();
        repository.delete(result)
                .test()
                .assertValues(true);

        repository.delete(result)
                .test()
                .assertValue(false);
    }

    @Test
    public void shouldNotInsertGeofenceWithoutData() {
        repository.insert(GeofenceData.create(null, 1, 1, 1))
                .test()
                .assertError(IllegalStateException.class)
                .assertNoValues();
    }

    @Test
    public void shouldUpdateGeofenceData() {
        GeofenceData geofence = repository.insert(testGeofence)
                .blockingGet();

        String newName = "Some newName";
        repository.update(geofence.withName(newName))
                .test()
                .assertValues(true);

        repository.listenGeofences()
                .test()
                .assertValueCount(1)
                .assertValueAt(0, testGeofences -> testGeofences.size() == 1 &&
                        testGeofences.get(0).equals(testGeofence.withName(newName)));
    }

    @Test
    public void shouldNotUpdateNonExistingGeofence() {
        repository.update(testGeofence)
                .test()
                .assertValues(false);
    }

    @Test
    public void shouldFetchKnownGeofencesWhenSubscribing() {
        repository.insert(testGeofence)
                .test()
                .assertValues(testGeofence.withId(1));

        repository.listenGeofences()
                .test()
                .assertValueCount(1)
                .assertValueAt(0, testGeofences -> testGeofences.size() == 1 &&
                        testGeofences.get(0).equals(testGeofence.withId(1)));

        GeofenceData geofenceTwo = GeofenceData.create("New Geofence", 2, 2, 2);
        repository.insert(geofenceTwo)
                .test()
                .assertValues(geofenceTwo.withId(2));

        repository.listenGeofences()
                .test()
                .assertValueCount(1)
                .assertValueAt(0, testGeofences -> testGeofences.size() == 2 &&
                        testGeofences.get(1).equals(testGeofence.withId(2)));
    }

    private void clearDatabase() {
        database.delete(GeofenceModel.TABLE_NAME, null);
    }
}