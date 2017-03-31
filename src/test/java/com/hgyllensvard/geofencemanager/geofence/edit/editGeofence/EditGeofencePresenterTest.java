package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.RxSchedulersOverriderRule;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofenceId;
import com.hgyllensvard.geofencemanager.toolbar.ToolbarTitle;
import com.hgyllensvard.geofencemanager.toolbar.ToolbarTitleManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.ID_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.LAT_LNG_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.NAME_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.TEST_GEOFENCE_ONE_WITH_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditGeofencePresenterTest {

    @Rule
    public final RxSchedulersOverriderRule rxSchedulersOverriderRule = new RxSchedulersOverriderRule();

    @Mock
    EditGeofencePresenterManager editGeofencePresenterManager;

    @Mock
    EditGeofenceViews editGeofenceViews;

    @Mock
    SelectedGeofenceId selectedGeofenceId;

    @Mock
    SelectedGeofence selectedGeofence;

    @Mock
    ToolbarTitleManager toolbarTitleManager;

    private EditGeofencePresenter editGeofencePresenter;

    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setupDefaultMocks();

        editGeofencePresenter = new EditGeofencePresenter(
                editGeofencePresenterManager,
                selectedGeofenceId,
                toolbarTitleManager,
                selectedGeofence);
    }

    @Test
    public void deleteGeofence_succeedToDelete_VerifyClosingView() {
        when(editGeofenceViews.observeDeleteGeofence()).thenReturn(Observable.just(true));
        when(selectedGeofence.delete()).thenReturn(Single.just(true));

        editGeofencePresenter.bindView(editGeofenceViews);

        verify(editGeofenceViews, times(1)).exitView();
    }

    @Test
    public void deleteGeofence_failToDelete_DoNotUpdateUI() {
        when(editGeofenceViews.observeDeleteGeofence()).thenReturn(Observable.just(true));
        when(selectedGeofence.delete()).thenReturn(Single.just(false));

        editGeofencePresenter.bindView(editGeofenceViews);

        verify(editGeofenceViews, never()).exitView();
    }

    @Test
    public void displaySelectedGeofenceOptions() {
        when(selectedGeofence.observeSelectedGeofence()).thenReturn(Observable.just(TEST_GEOFENCE_ONE_WITH_ID));

        editGeofencePresenter.bindView(editGeofenceViews);

        verify(editGeofenceViews, times(1)).displaySelectedGeofenceOptions();
    }

    @Test
    public void unbindView_SaveGeofence() {
        when(selectedGeofenceId.isGeofenceSelected()).thenReturn(true);
        when(selectedGeofenceId.selectedGeofenceId()).thenReturn(ID_ONE);
        when(editGeofenceViews.getGeofencePosition(ID_ONE)).thenReturn(LAT_LNG_ONE);
        when(toolbarTitleManager.title()).thenReturn(ToolbarTitle.create(NAME_ONE));

        editGeofencePresenter.bindView(editGeofenceViews);
        editGeofencePresenter.unbindView();

        verify(editGeofencePresenterManager, times(1)).updateSelectedGeofence(NAME_ONE, LAT_LNG_ONE);
    }

    @Test
    public void unbindView_ViewNull_DoNotAttemptToSave() {
        editGeofencePresenter.unbindView();

        verify(editGeofencePresenterManager, never()).updateSelectedGeofence(any(String.class), any(LatLng.class));
    }


    @Test
    public void unbindView_NoValidGeofenceSelected_DoNotAttemptToSave() {
        editGeofencePresenter.bindView(editGeofenceViews);

        when(selectedGeofenceId.isGeofenceSelected()).thenReturn(false);
        editGeofencePresenter.unbindView();

        verify(editGeofencePresenterManager, never()).updateSelectedGeofence(any(String.class), any(LatLng.class));
    }

    /**
     * Used to avoid mocking these every time and the specific method testing will override it.
     */
    private void setupDefaultMocks() {
        when(editGeofenceViews.displayMap()).thenReturn(Observable.just(true));
        when(editGeofenceViews.observeCameraStartedMoving()).thenReturn(PublishSubject.create());
        when(editGeofenceViews.observeDeleteGeofence()).thenReturn(PublishSubject.create());
        when(editGeofenceViews.observeGeofenceSelected()).thenReturn(PublishSubject.create());

        when(selectedGeofence.observeSelectedGeofence()).thenReturn(PublishSubject.create());
    }
}