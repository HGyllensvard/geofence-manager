package com.hgyllensvard.geofencemanager.toolbar;

import com.hgyllensvard.geofencemanager.geofence.RxSchedulersOverriderRule;
import com.hgyllensvard.geofencemanager.geofence.SelectedGeofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.Geofence;
import com.hgyllensvard.geofencemanager.geofence.geofence.GeofenceManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.ID_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.NAME_ONE;
import static com.hgyllensvard.geofencemanager.geofence.GeofenceTestHelper.TEST_GEOFENCE_ONE;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
    GeofenceManager geofenceManager;

    @Mock
    EditableToolbarView view;

    private EditableTitleToolbarPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        setupDummyInitialStates();

        presenter = new EditableTitleToolbarPresenter(
                selectedGeofence,
                geofenceManager);
    }

    @Test
    public void bindView_subscribeTitleChanges_updateLocalTitle() {
        when(view.observeTitle()).thenReturn(Observable.just(TOOLBAR_TITLE));

        assertThat(presenter.title()).isNull();

        presenter.bindView(view);

        assertThat(presenter.title().title()).isEqualTo(TITLE);
    }

    @Test
    public void bindView_getSelectedGeofence_updateViewTitle() {
        when(selectedGeofence.selectedGeofence()).thenReturn(ID_ONE);
        when(geofenceManager.getGeofence(ID_ONE)).thenReturn(Single.just(TEST_GEOFENCE_ONE));

        presenter.bindView(view);

        verify(view, times(1)).title(NAME_ONE);
    }

    @Test
    public void bindView_geofenceDoesNotExist_doNotUpdateView() {
        when(selectedGeofence.selectedGeofence()).thenReturn(ID_ONE);
        when(geofenceManager.getGeofence(ID_ONE)).thenReturn(Single.just(Geofence.sDummyGeofence));

        presenter.bindView(view);

        verify(view, never()).title(any(String.class));
    }

    private void setupDummyInitialStates() {
        when(view.observeTitle()).thenReturn(PublishSubject.create());
        when(geofenceManager.getGeofence(any(Long.class))).thenReturn(Single.just(Geofence.sDummyGeofence));
    }
}