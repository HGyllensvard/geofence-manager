package com.hgyllensvard.geofencemanager.geofence.playIntegration;

import com.google.android.gms.location.GeofencingEvent;
import com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GeofenceTriggeredManagerTest {

    @Mock
    GeofenceManager geofenceManager;

    @Mock
    GeofencingEvent event;

    private String idOneStr = "1";
    private long idOne = 1;

    private String idTwoStr = "2";
    private long idTwo = 2;

    private final int triggerEnter = com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_ENTER;
    private List<com.google.android.gms.location.Geofence> googleGeofences;

    private GeofenceTriggeredManager geofenceEventTriggerManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());

        googleGeofences = new ArrayList<>();

        geofenceEventTriggerManager = new GeofenceTriggeredManager(geofenceManager);
    }

    @Test
    public void forwardTriggeredEnteredGeofences() {
        setupGeofenceManagerMock();
        assertTriggerTypeForwarded(geofenceEventTriggerManager.observeEnteredGeofences().test(), triggerEnter);
    }

    @Test
    public void forwardTriggeredDwellingGeofences() {
        setupGeofenceManagerMock();
        assertTriggerTypeForwarded(geofenceEventTriggerManager.observeGeofencesDwelling().test(), com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_DWELL);
    }

    @Test
    public void forwardTriggeredExitingGeofences() {
        setupGeofenceManagerMock();
        assertTriggerTypeForwarded(geofenceEventTriggerManager.observeLeftGeofence().test(), com.google.android.gms.location.Geofence.GEOFENCE_TRANSITION_EXIT);
    }

    @Test
    public void doNotForwardGeofenceWithInvalidId() {
        googleGeofences.add(buildGeofence("INVALID", triggerEnter));

        when(event.getTriggeringGeofences()).thenReturn(googleGeofences);
        mockGeofenceTransitionTrigger(triggerEnter);

        TestObserver<Geofence> testSubscriber = geofenceEventTriggerManager.observeEnteredGeofences().test();

        geofenceEventTriggerManager.geofencesTriggered(event);

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertNoValues();
    }

    @Test
    public void doNotForwardNonExistingGeofence() {
        setupGoogleGeofences(triggerEnter);
        when(event.getTriggeringGeofences()).thenReturn(googleGeofences);
        mockGeofenceTransitionTrigger(triggerEnter);

        when(geofenceManager.getGeofence(any(Long.class))).thenReturn(Single.error(new RuntimeException()));

        TestObserver<Geofence> testSubscriber = geofenceEventTriggerManager.observeEnteredGeofences().test();

        geofenceEventTriggerManager.geofencesTriggered(event);

        testSubscriber.awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertNoValues();
    }

    private void assertTriggerTypeForwarded(TestObserver<Geofence> testSubscriber, int triggerType) {
        setupGoogleGeofences(triggerType);
        when(event.getTriggeringGeofences()).thenReturn(googleGeofences);
        mockGeofenceTransitionTrigger(triggerType);

        geofenceEventTriggerManager.geofencesTriggered(event);

        testSubscriber
                .awaitDone(1, TimeUnit.SECONDS)
                .assertNoErrors()
                .assertValueCount(2)
                .assertValues(GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID,
                        GeofenceTestHelper.TEST_GEOFENCE_TWO_WITH_ID);
    }

    private void setupGoogleGeofences(int transitionType) {
        googleGeofences.add(buildGeofence(idOneStr, transitionType));
        googleGeofences.add(buildGeofence(idTwoStr, transitionType));
    }

    private com.google.android.gms.location.Geofence buildGeofence(String requestId, int triggerType) {
        return new com.google.android.gms.location.Geofence.Builder()
                .setRequestId(requestId)
                .setTransitionTypes(triggerType)
                .setExpirationDuration(com.google.android.gms.location.Geofence.NEVER_EXPIRE)
                .setCircularRegion(1, 1, 1)
                .setLoiteringDelay(10)
                .build();
    }

    private void setupGeofenceManagerMock() {
        when(geofenceManager.getGeofence(idOne)).thenReturn(Single.just(GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID));
        when(geofenceManager.getGeofence(idTwo)).thenReturn(Single.just(GeofenceTestHelper.TEST_GEOFENCE_TWO_WITH_ID));
    }

    private void mockGeofenceTransitionTrigger(int geofenceTransitionTrigger) {
        when(event.getGeofenceTransition()).thenReturn(geofenceTransitionTrigger);
    }
}