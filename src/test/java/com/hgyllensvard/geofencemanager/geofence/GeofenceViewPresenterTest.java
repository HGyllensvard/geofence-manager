package com.hgyllensvard.geofencemanager.geofence;

import android.support.v7.app.AppCompatActivity;

import com.hgyllensvard.geofencemanager.geofence.permission.LocationManager;
import com.hgyllensvard.geofencemanager.geofence.permission.RequestPermissionResult;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceMapOptions;
import com.hgyllensvard.geofencemanager.geofence.view.GeofenceViews;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.processors.ReplayProcessor;
import io.reactivex.schedulers.Schedulers;
import rx.plugins.RxJavaHooks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class GeofenceViewPresenterTest {
    
    @Mock
    AppCompatActivity appCompatActivity;

    @Mock
    LocationManager locationManager;

    @Mock
    GeofenceManager geofenceManager;

    @Mock
    GeofenceMapOptions geofenceMapOptions;

    @Mock
    GeofenceViews geofenceViews;

    @Mock
    MapCameraManager mapCameraManager;

    private GeofenceViewPresenter geofenceViewPresenter;

    @BeforeClass
    public static void beforeClass() {
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        geofenceViewPresenter = new GeofenceViewPresenter(
                appCompatActivity,
                locationManager,
                geofenceManager,
                geofenceMapOptions,
                mapCameraManager);
    }

    @After
    public void tearDown() {
        geofenceViewPresenter.unbindView();

        RxJavaHooks.reset();
    }

    @Test
    public void shouldFinishActivityIfNoLocationPermissionIsGiven() {
        when(locationManager.request()).thenReturn(Single.just(RequestPermissionResult.DENIED));

        geofenceViewPresenter.bindView(geofenceViews);

        verify(appCompatActivity, times(1)).finish();
        verifyNoMoreInteractions(appCompatActivity);
    }

    @Test
    public void shouldDisplayMapAndRegisterRelatedSubscriptionsWhenLocationAccessIsGiven() {
        when(locationManager.request()).thenReturn(Single.just(RequestPermissionResult.GRANTED));
        when(geofenceViews.displayMap()).thenReturn(Single.just(true));
        when(geofenceViews.observerLongClick()).thenReturn(ReplayProcessor.create());
        when(geofenceManager.observeGeofences()).thenReturn(ReplayProcessor.create());

        geofenceViewPresenter.bindView(geofenceViews);

        assertThat(geofenceViewPresenter.getSubscriptions().size()).isEqualTo(4);
    }
}