package com.hgyllensvard.geofencemanager.toolbar;

import com.hgyllensvard.geofencemanager.RxSchedulersOverriderRule;
import com.hgyllensvard.geofencemanager.geofence.selectedGeofence.SelectedGeofence;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.NAME_ONE;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceTestHelper.GEOFENCE_ONE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EditableTitleToolbarPresenterTest {

    @Rule
    public final RxSchedulersOverriderRule rxSchedulersOverriderRule = new RxSchedulersOverriderRule();

    private static final String TITLE = "title";
    private static final ToolbarTitle TOOLBAR_TITLE = ToolbarTitle.create(TITLE);

    @Mock
    SelectedGeofence selectedGeofence;

    @Mock
    ToolbarTitleManager toolbarTitleManager;

    @Mock
    EditableToolbarView view;

    private EditableTitleToolbarPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setupDummyInitialStates();

        presenter = new EditableTitleToolbarPresenter(
                selectedGeofence,
                toolbarTitleManager);
    }

    @Test
    public void bindView_subscribeTitleChanges_updateLocalTitle() {
        when(view.observeTitle()).thenReturn(Observable.just(TITLE));

        presenter.bindView(view);

        verify(toolbarTitleManager, times(1)).title(TOOLBAR_TITLE);
    }

    @Test
    public void bindView_getSelectedGeofence_updateViewTitle() {
        when(selectedGeofence.observeValidSelectedGeofence()).thenReturn(Observable.just(GEOFENCE_ONE));

        presenter.bindView(view);

        verify(view, times(1)).title(NAME_ONE);
    }

    private void setupDummyInitialStates() {
        when(view.observeTitle()).thenReturn(PublishSubject.create());
        when(selectedGeofence.observeValidSelectedGeofence()).thenReturn(PublishSubject.create());
    }
}