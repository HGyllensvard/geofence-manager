package com.hgyllensvard.geofencemanager.geofence.edit.map;

import com.hgyllensvard.geofencemanager.geofence.edit.map.geofenceView.GeofenceView;
import com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceViewTestHelper;
import com.hgyllensvard.geofencemanager.RxSchedulersOverriderRule;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subscribers.TestSubscriber;

import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceMapOptionsTestHelper.FILL_COLOUR;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceMapOptionsTestHelper.GEOFENCE_MAP_OPTIONS;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceMapOptionsTestHelper.STROKE_COLOUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GeofenceViewManagerTest {

    @Rule
    public final RxSchedulersOverriderRule logger = new RxSchedulersOverriderRule();

    @Mock
    GeofenceManager geofenceManager;

    private SelectedGeofenceId selectedGeofenceId;

    private GeofenceViewManager geofenceViewManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        selectedGeofenceId = new SelectedGeofenceId();

        geofenceViewManager = new GeofenceViewManager(
                geofenceManager,
                GEOFENCE_MAP_OPTIONS,
                selectedGeofenceId);
    }

    @Test
    public void successfullyAddGeofenceViews() {
        when(geofenceManager.observeGeofences()).thenReturn(Flowable.just(GeofenceTestHelper.TEST_GEOFENCES_WITH_ID));

        selectedGeofenceId.selectedGeofence(GeofenceTestHelper.ID_ONE);

        GeofenceViewUpdate geofenceViewUpdate = getGeofenceViewUpdate(geofenceViewManager.observeGeofenceViews().test(), 0);

        assertThat(geofenceViewUpdate.selectedGeofenceViews()).contains(GeofenceViewTestHelper.GEOFENCE_VIEW_ONE);
        assertThat(geofenceViewUpdate.geofenceViews()).contains(GeofenceViewTestHelper.GEOFENCE_VIEW_ONE);
        assertThat(geofenceViewUpdate.removedGeofenceViews()).isEmpty();
    }

    @Test
    public void removeGeofenceViewPreviouslyAdded() {
        PublishProcessor<List<Geofence>> subject = PublishProcessor.create();

        when(geofenceManager.observeGeofences()).thenReturn(subject);

        selectedGeofenceId.selectedGeofence(GeofenceTestHelper.ID_ONE);

        TestSubscriber<GeofenceViewUpdate> testSubscriber = geofenceViewManager.observeGeofenceViews().test();

        subject.onNext(GeofenceTestHelper.TEST_GEOFENCES_WITH_ID);
        subject.onNext(Collections.singletonList(GeofenceTestHelper.GEOFENCE_ONE));

        GeofenceViewUpdate geofenceViewUpdate = getGeofenceViewUpdate(testSubscriber, 1);

        assertThat(geofenceViewUpdate.geofenceViews()).contains(GeofenceViewTestHelper.GEOFENCE_VIEW_ONE);
        assertThat(geofenceViewUpdate.selectedGeofenceViews()).isEmpty();
        assertThat(geofenceViewUpdate.removedGeofenceViews()).isEmpty();
    }

    @Test
    public void updatePreviouslyAddedGeofenceView() {
        PublishProcessor<List<Geofence>> subject = PublishProcessor.create();

        when(geofenceManager.observeGeofences()).thenReturn(subject);
        selectedGeofenceId.selectedGeofence(GeofenceTestHelper.ID_ONE);

        TestSubscriber<GeofenceViewUpdate> testSubscriber = geofenceViewManager.observeGeofenceViews()
                .test();

        Geofence updatedGeofence = GeofenceTestHelper.GEOFENCE_ONE.withName("A new Name");
        subject.onNext(Collections.singletonList(GeofenceTestHelper.GEOFENCE_ONE));
        subject.onNext(Collections.singletonList(updatedGeofence));

        GeofenceViewUpdate geofenceViewUpdate = getGeofenceViewUpdate(testSubscriber, 1);

        GeofenceView updatedGeofenceView = GeofenceView.create(updatedGeofence, FILL_COLOUR, STROKE_COLOUR);
        assertThat(geofenceViewUpdate.geofenceViews()).contains(updatedGeofenceView);
        assertThat(geofenceViewUpdate.selectedGeofenceViews()).contains(updatedGeofenceView);
        assertThat(geofenceViewUpdate.removedGeofenceViews()).isEmpty();
    }

    private GeofenceViewUpdate getGeofenceViewUpdate(TestSubscriber<GeofenceViewUpdate> testSubscriber, int index) {
        return testSubscriber
                .assertNoErrors()
                .values()
                .get(index);
    }
}