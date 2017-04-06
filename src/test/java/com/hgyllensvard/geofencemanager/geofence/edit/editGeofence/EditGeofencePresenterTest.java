package com.hgyllensvard.geofencemanager.geofence.edit.editGeofence;

import com.hgyllensvard.geofencemanager.RxSchedulersOverriderRule;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceActionResult;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;
import com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper;
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

import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceActionResultTestHelper.SUCCESS_SINGLE_GEOFENCE_ONE;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.GEOFENCE_ONE;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.GEOFENCE_ONE_STATE_NO_ID;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.ID_ONE;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.NAME_TWO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditGeofencePresenterTest {

    @Rule
    public final RxSchedulersOverriderRule rxSchedulersOverriderRule = new RxSchedulersOverriderRule();

    @Mock
    GeofenceManager geofenceManager;

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
                selectedGeofenceId,
                toolbarTitleManager,
                selectedGeofence,
                geofenceManager);
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
        when(selectedGeofence.observeSelectedGeofence()).thenReturn(Observable.just(GEOFENCE_ONE_STATE_NO_ID));

        editGeofencePresenter.bindView(editGeofenceViews);

        verify(editGeofenceViews, times(1)).displaySelectedGeofenceOptions();
    }

    @Test
    public void unbindView_SaveGeofence_bothTitleAndGeofenceExist() {
        when(selectedGeofenceId.isGeofenceSelected()).thenReturn(true);
        when(selectedGeofenceId.selectedGeofenceId()).thenReturn(ID_ONE);
        when(editGeofenceViews.getGeofencePosition(ID_ONE)).thenReturn(GeofenceTestHelper.GEOFENCE_ONE);
        when(toolbarTitleManager.title()).thenReturn(ToolbarTitle.create(NAME_TWO));

        Geofence updatedGeofence = GEOFENCE_ONE.withName(NAME_TWO);
        when(geofenceManager.updateGeofence(updatedGeofence)).thenReturn(Single.just(GeofenceActionResult.success(updatedGeofence)));

        editGeofencePresenter.bindView(editGeofenceViews);
        editGeofencePresenter.unbindView();

        verify(geofenceManager, times(1)).updateGeofence(updatedGeofence);
    }

    @Test
    public void unbindView_SaveGeofence_onlyGeofenceExist() {
        when(selectedGeofenceId.isGeofenceSelected()).thenReturn(true);
        when(selectedGeofenceId.selectedGeofenceId()).thenReturn(ID_ONE);
        when(editGeofenceViews.getGeofencePosition(ID_ONE)).thenReturn(GeofenceTestHelper.GEOFENCE_ONE);
        when(toolbarTitleManager.title()).thenReturn(null);
        when(geofenceManager.updateGeofence(GEOFENCE_ONE)).thenReturn(SUCCESS_SINGLE_GEOFENCE_ONE);

        editGeofencePresenter.bindView(editGeofenceViews);
        editGeofencePresenter.unbindView();

        verify(geofenceManager, times(1)).updateGeofence(GEOFENCE_ONE);
    }

    @Test
    public void unbindView_ViewNull_DoNotAttemptToSave() {
        editGeofencePresenter.unbindView();

        verify(geofenceManager, never()).updateGeofence(any(Geofence.class));
    }


    @Test
    public void unbindView_NoValidGeofenceSelected_DoNotAttemptToSave() {
        editGeofencePresenter.bindView(editGeofenceViews);

        when(selectedGeofenceId.isGeofenceSelected()).thenReturn(false);
        editGeofencePresenter.unbindView();

        verify(geofenceManager, never()).updateGeofence(any(Geofence.class));
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