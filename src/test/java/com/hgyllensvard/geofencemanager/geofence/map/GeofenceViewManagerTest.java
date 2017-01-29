package com.hgyllensvard.geofencemanager.geofence.map;

import com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.geofence.GeofenceViewTestHelper;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GeofenceViewManagerTest {

    @Mock
    GeofenceMapOptions geofenceMapOptions;

    @Mock
    GeofenceManager geofenceManager;

    private GeofenceViewManager geofenceViewManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(geofenceMapOptions.fillColor()).thenReturn(GeofenceViewTestHelper.FILL_COLOR);
        when(geofenceMapOptions.strokeColor()).thenReturn(GeofenceViewTestHelper.STROKE_COLOR);

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());

        geofenceViewManager = new GeofenceViewManager(
                geofenceManager,
                geofenceMapOptions);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void successfullyAddGeofenceViews() {
        when(geofenceManager.observeGeofences()).thenReturn(Flowable.just(GeofenceTestHelper.TEST_GEOFENCES_WITH_ID));

        GeofenceViewUpdate geofenceViewUpdate = getGeofenceViewUpdate(geofenceViewManager.observeGeofenceViews().test(), 0);

        assertThat(geofenceViewUpdate.updatedGeofenceViews()).contains(GeofenceViewTestHelper.GEOFENCE_VIEW_ONE, GeofenceViewTestHelper.GEOFENCE_VIEW_TWO);
        assertThat(geofenceViewUpdate.geofenceViews()).contains(GeofenceViewTestHelper.GEOFENCE_VIEW_ONE, GeofenceViewTestHelper.GEOFENCE_VIEW_TWO);
        assertThat(geofenceViewUpdate.removedGeofenceViews()).isEmpty();
    }

    @Test
    public void removeGeofenceViewPreviouslyAdded() {
        PublishProcessor<List<Geofence>> subject = PublishProcessor.create();

        when(geofenceManager.observeGeofences()).thenReturn(subject);

        TestSubscriber<GeofenceViewUpdate> testSubscriber = geofenceViewManager.observeGeofenceViews().test();

        subject.onNext(GeofenceTestHelper.TEST_GEOFENCES_WITH_ID);
        subject.onNext(Collections.singletonList(GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID));

        GeofenceViewUpdate geofenceViewUpdate = getGeofenceViewUpdate(testSubscriber, 1);

        assertThat(geofenceViewUpdate.geofenceViews()).contains(GeofenceViewTestHelper.GEOFENCE_VIEW_ONE);
        assertThat(geofenceViewUpdate.updatedGeofenceViews()).isEmpty();
        assertThat(geofenceViewUpdate.removedGeofenceViews()).contains(GeofenceViewTestHelper.GEOFENCE_VIEW_TWO);
    }

    @Test
    public void updatePreviouslyAddedGeofenceView() {
        PublishProcessor<List<Geofence>> subject = PublishProcessor.create();

        when(geofenceManager.observeGeofences()).thenReturn(subject);

        TestSubscriber<GeofenceViewUpdate> testSubscriber = geofenceViewManager.observeGeofenceViews().test();

        Geofence updatedGeofence = GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID.withName("A new Name");
        subject.onNext(Collections.singletonList(GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID));
        subject.onNext(Collections.singletonList(updatedGeofence));

        GeofenceViewUpdate geofenceViewUpdate = getGeofenceViewUpdate(testSubscriber, 1);

        GeofenceView updatedGeofenceView = new GeofenceView(updatedGeofence, geofenceMapOptions.fillColor(), geofenceMapOptions.strokeColor());
        assertThat(geofenceViewUpdate.geofenceViews()).contains(updatedGeofenceView);
        assertThat(geofenceViewUpdate.updatedGeofenceViews()).contains(updatedGeofenceView);
        assertThat(geofenceViewUpdate.removedGeofenceViews()).isEmpty();
    }

    private GeofenceViewUpdate getGeofenceViewUpdate(TestSubscriber<GeofenceViewUpdate> testSubscriber, int index) {
        return testSubscriber
                .assertNoErrors()
                .values()
                .get(index);
    }
}