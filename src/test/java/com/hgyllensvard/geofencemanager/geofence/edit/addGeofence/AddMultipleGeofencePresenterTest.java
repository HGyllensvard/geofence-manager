package com.hgyllensvard.geofencemanager.geofence.edit.addGeofence;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceActionResult;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddMultipleGeofencePresenterTest {

    @Mock
    GeofenceManager geofenceManager;

    @Mock
    AddGeofenceViews addGeofenceViews;

    private SelectedGeofence selectedGeofence;

    private AddGeofencePresenter addGeofencePresenter;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());

        selectedGeofence = new SelectedGeofence(-1);

        addGeofencePresenter = new AddGeofencePresenter(geofenceManager, GeofenceTestHelper.GEOFENCE_MAP_OPTIONS, selectedGeofence);
    }

    @After
    public void tearDown() throws Exception {
        addGeofencePresenter.unbindView();
    }

    @Test
    public void shouldAddGeofenceWhenLockClickOccurs() {
        PublishSubject<LatLng> subject = PublishSubject.create();

        when(addGeofenceViews.observerLongClick()).thenReturn(subject);
        when(addGeofenceViews.displayMap()).thenReturn(Observable.just(true));
        when(geofenceManager.addGeofence(any(Geofence.class))).thenReturn(Single.just(Mockito.mock(GeofenceActionResult.class)));

        addGeofencePresenter.bindView(addGeofenceViews);

        subject.onNext(GeofenceTestHelper.LAT_LNG_ONE);

        verify(geofenceManager, times(1)).addGeofence(GeofenceTestHelper.TEST_GEOFENCE_ONE);
    }

    @Test
    public void shouldUnsubscribeWhenUnbinding() {
        PublishSubject<Boolean> displayMapSubject = PublishSubject.create();
        PublishSubject<LatLng> longClickSubject = PublishSubject.create();

        when(addGeofenceViews.displayMap()).thenReturn(displayMapSubject);
        when(addGeofenceViews.observerLongClick()).thenReturn(longClickSubject);

        addGeofencePresenter.bindView(addGeofenceViews);

        displayMapSubject.onNext(true);

        assertThat(addGeofencePresenter.disposableContainer.size()).isEqualTo(2);

        addGeofencePresenter.unbindView();

        assertThat(addGeofencePresenter.disposableContainer.size()).isZero();
    }
}