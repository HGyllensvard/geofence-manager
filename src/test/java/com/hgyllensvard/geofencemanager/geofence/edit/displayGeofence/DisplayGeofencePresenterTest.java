package com.hgyllensvard.geofencemanager.geofence.edit.displayGeofence;

import com.hgyllensvard.geofencemanager.geofence.GeofenceViewTestHelper;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceViewManager;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceViewUpdate;
import com.hgyllensvard.geofencemanager.geofence.edit.map.MapCameraManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DisplayGeofencePresenterTest {

    @Mock
    GeofenceViewManager geofenceViewManager;

    @Mock
    MapCameraManager mapCameraManager;

    @Mock
    DisplayGeofenceViews displayGeofenceViews;

    private DisplayGeofencePresenter displayGeofencePresenter;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());

        when(displayGeofenceViews.displayMap()).thenReturn(Observable.just(true));

        displayGeofencePresenter = new DisplayGeofencePresenter(geofenceViewManager, mapCameraManager);
    }

    @After
    public void tearDown() {
        displayGeofencePresenter.unbindView();

        assertThat(displayGeofencePresenter.disposableContainer.size()).isZero();
    }

    @Test
    public void zoomToUserPositionWhenMapIsDisplayed() {
        displayGeofencePresenter.bindView(displayGeofenceViews);

//         CameraUpdate is final so can't mock, the important is to know that the UI was called
        verify(displayGeofenceViews, times(1)).animateCameraTo(null);
    }

    @Test
    public void successfullyUpdateMapWithGeofencesWhenMapIsDisplayed() {
        when(geofenceViewManager.observeGeofenceViews()).thenReturn(Flowable.just(GeofenceViewTestHelper.TEST_GEOFENCE_VIEW_UPDATE));

        displayGeofencePresenter.bindView(displayGeofenceViews);

        verify(displayGeofenceViews, times(1)).displayGeofenceViews(GeofenceViewTestHelper.GEOFENCE_VIEWS_UPDATED);
        verify(displayGeofenceViews, times(1)).removeGeofenceViews(GeofenceViewTestHelper.GEOFENCE_VIEWS_REMOVED);
    }

    @Test
    public void unsubscribeWhenUnbinding() {
        when(geofenceViewManager.observeGeofenceViews()).thenReturn(PublishProcessor.create());

        displayGeofencePresenter.bindView(displayGeofenceViews);

        assertThat(displayGeofencePresenter.disposableContainer.size()).isEqualTo(2);
    }

    @Test
    public void observeGeofenceViewsErrorManagement() {
        PublishProcessor<GeofenceViewUpdate> publishProcessor = PublishProcessor.create();
        when(geofenceViewManager.observeGeofenceViews()).thenReturn(publishProcessor);

        displayGeofencePresenter.bindView(displayGeofenceViews);

        publishProcessor.onError(new RuntimeException());

        verify(displayGeofenceViews, never()).displayGeofenceViews(GeofenceViewTestHelper.GEOFENCE_VIEWS_UPDATED);
        verify(displayGeofenceViews, never()).removeGeofenceViews(GeofenceViewTestHelper.GEOFENCE_VIEWS_REMOVED);

        assertThat(displayGeofencePresenter.disposableContainer.size()).isEqualTo(2);
        assertThat(displayGeofencePresenter.disposableContainer.isDisposed()).isFalse();
    }
}