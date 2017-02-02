package com.hgyllensvard.geofencemanager.geofence.editGeofence;

import com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditGeofencePresenterTest {

    @Mock
    GeofenceManager geofenceManager;

    @Mock
    EditGeofenceViews editGeofenceViews;

    private EditGeofencePresenter editGeofencePresenter;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());

        setupDefaultMocks();

        editGeofencePresenter = new EditGeofencePresenter(geofenceManager);
    }

    @Test
    public void selectGeofenceAndSetAsCurrentlyActiveGeofence() {
        when(editGeofenceViews.observeGeofenceSelected()).thenReturn(Observable.just(GeofenceTestHelper.ID_ONE));

        editGeofencePresenter.bindView(editGeofenceViews);

        verify(editGeofenceViews, times(1)).displaySelectedGeofenceOptions();
        assertThat(editGeofencePresenter.selectedGeofenceId).isEqualTo(GeofenceTestHelper.ID_ONE);
    }

    @Test
    public void doNotReselectAlreadySelectedGeofence() {
        PublishSubject<Long> subject = PublishSubject.create();

        when(editGeofenceViews.observeGeofenceSelected()).thenReturn(subject);

        editGeofencePresenter.bindView(editGeofenceViews);

        subject.onNext(GeofenceTestHelper.ID_ONE);
        subject.onNext(GeofenceTestHelper.ID_ONE);

        verify(editGeofenceViews, times(1)).displaySelectedGeofenceOptions();
        assertThat(editGeofencePresenter.selectedGeofenceId).isEqualTo(GeofenceTestHelper.ID_ONE);
    }

    @Test
    public void deselectGeofence() {
        editGeofencePresenter.selectedGeofenceId = GeofenceTestHelper.ID_ONE;
        when(editGeofenceViews.observeCameraStartedMoving()).thenReturn(Observable.just(1));

        editGeofencePresenter.bindView(editGeofenceViews);

        verify(editGeofenceViews, times(2)).hideSelectedGeofenceOptions();
        assertThat(editGeofencePresenter.selectedGeofenceId).isEqualTo(EditGeofencePresenter.NO_SELECTION);
    }

    @Test
    public void doNotDeselectWhenNothingIsSelected() {
        editGeofencePresenter.selectedGeofenceId = GeofenceTestHelper.ID_ONE;
        PublishSubject<Integer> subject = PublishSubject.create();

        when(editGeofenceViews.observeCameraStartedMoving()).thenReturn(subject);

        editGeofencePresenter.bindView(editGeofenceViews);

        subject.onNext(1);
        subject.onNext(2);

        verify(editGeofenceViews, times(1)).observeCameraStartedMoving();
        assertThat(editGeofencePresenter.selectedGeofenceId).isEqualTo(EditGeofencePresenter.NO_SELECTION);
    }

    @Test
    public void deleteGeofence() {
        editGeofencePresenter.selectedGeofenceId = GeofenceTestHelper.ID_ONE;
        when(editGeofenceViews.observeDeleteGeofence()).thenReturn(Observable.just(true));
        when(geofenceManager.removeGeofence(GeofenceTestHelper.ID_ONE)).thenReturn(Single.just(true));

        editGeofencePresenter.bindView(editGeofenceViews);

        verify(editGeofenceViews, times(2)).hideSelectedGeofenceOptions();
        verify(geofenceManager, times(1)).removeGeofence(GeofenceTestHelper.ID_ONE);

        assertThat(editGeofencePresenter.selectedGeofenceId).isEqualTo(EditGeofencePresenter.NO_SELECTION);
    }

    @Test
    public void doNotUpdateUIIfFailToDeleteGeofence() {
        editGeofencePresenter.selectedGeofenceId = GeofenceTestHelper.ID_ONE;
        when(editGeofenceViews.observeDeleteGeofence()).thenReturn(Observable.just(true));
        when(geofenceManager.removeGeofence(GeofenceTestHelper.ID_ONE)).thenReturn(Single.just(false));

        editGeofencePresenter.bindView(editGeofenceViews);

        assertThat(editGeofencePresenter.selectedGeofenceId).isEqualTo(GeofenceTestHelper.ID_ONE);
        verify(editGeofenceViews, times(1)).hideSelectedGeofenceOptions();
        verify(geofenceManager, times(1)).removeGeofence(GeofenceTestHelper.ID_ONE);
    }

    @Test
    public void doNotDeleteIfNoSelection() {
        editGeofencePresenter.bindView(editGeofenceViews);

        assertThat(editGeofencePresenter.selectedGeofenceId).isEqualTo(EditGeofencePresenter.NO_SELECTION);
        verify(geofenceManager, never()).removeGeofence(any(Long.class));
    }

    /**
     * Used to avoid mocking these every time and the specific method testing will override it.
     */
    private void setupDefaultMocks() {
        when(editGeofenceViews.displayMap()).thenReturn(Observable.just(true));
        when(editGeofenceViews.observeCameraStartedMoving()).thenReturn(PublishSubject.create());
        when(editGeofenceViews.observeDeleteGeofence()).thenReturn(PublishSubject.create());
        when(editGeofenceViews.observeGeofenceSelected()).thenReturn(PublishSubject.create());
    }
}