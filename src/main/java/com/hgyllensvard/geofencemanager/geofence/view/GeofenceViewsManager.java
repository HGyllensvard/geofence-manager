package com.hgyllensvard.geofencemanager.geofence.view;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.R2;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.processors.PublishProcessor;
import timber.log.Timber;

public class GeofenceViewsManager implements GeofenceViews {

    private final AppCompatActivity activity;
    private final GeofenceMapOptions mapOptions;
    private final SupportMapFragment mapFragment;
    private final Single<Boolean> displayMap;
    private final Map<GeofenceData, GeofenceMarker> markers;
    private final PublishProcessor<SelectedGeofence> selectedGeofenceSubject;

    private Flowable<LatLng> longClickFlowable;

    private GoogleMap googleMap;
    private Unbinder unbinder;

    @BindView(R2.id.geofence_map_selected_geofence)
    View selecedGeofenceOptions;

    public GeofenceViewsManager(
            AppCompatActivity activity,
            GeofenceMapOptions mapOptions
    ) {
        this.activity = activity;
        this.mapOptions = mapOptions;

        unbinder = ButterKnife.bind(this, activity);

        markers = new HashMap<>();
        selectedGeofenceSubject = PublishProcessor.create();

        mapFragment = SupportMapFragment.newInstance();

        displayMap = createMapObserver();
    }

    @Override
    public Single<Boolean> displayMap() {
        return displayMap
                .doOnDispose(() ->
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .remove(mapFragment)
                                .commit())
                .doOnSuccess(disposable -> {
                    try {
                        googleMap.setMyLocationEnabled(true);
                    } catch (SecurityException e) {
                        Timber.e(e, "Unexpected error");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void animateCameraTo(CameraUpdate cameraUpdate) {
        googleMap.animateCamera(cameraUpdate);
    }

    /**
     * Will be null until the displayMap has returned
     */
    @Override
    @Nullable
    public Flowable<LatLng> observerLongClick() {
        return longClickFlowable;
    }

    @Override
    public void displayGeofences(List<GeofenceData> geofenceDatas) {
        for (Map.Entry<GeofenceData, GeofenceMarker> geofenceDataMarkerEntry : markers.entrySet()) {
            geofenceDataMarkerEntry.getValue().remove();
        }

        markers.clear();

        for (GeofenceData geofenceData : geofenceDatas) {
            GeofenceMarker marker = new GeofenceMarker(geofenceData,
                    mapOptions.fillColor(),
                    mapOptions.strokeColor());

            marker.display(googleMap);
            markers.put(geofenceData, marker);
        }
    }

    @Override
    public Flowable<SelectedGeofence> observeGeofenceSelected() {
        return selectedGeofenceSubject;
    }

    @Override
    public void destroy() {
        unbinder.unbind();
    }

    private Single<Boolean> createMapObserver() {
        return Single.create(e -> {
            addMapFragment();

            mapFragment.getMapAsync(map -> {
                googleMap = map;
                longClickFlowable = createLongPressEmitter();
                selectedMarkerListener();
                cameraMoveStartedListener();
                e.onSuccess(true);
            });

        });
    }

    private void cameraMoveStartedListener() {
        googleMap.setOnCameraMoveStartedListener(i ->
                selectedGeofenceSubject.onNext(SelectedGeofence.noGeofenceSelected()));
    }

    private void selectedMarkerListener() {
        googleMap.setOnMarkerClickListener(marker -> {
            for (Map.Entry<GeofenceData, GeofenceMarker> entry : markers.entrySet()) {
                if (entry.getValue().isMarker(marker.getId())) {
                    selectedGeofenceSubject.onNext(SelectedGeofence.geofenceSelected(entry.getKey()));
                    return true;
                }
            }

            return false;
        });
    }

    @Override
    public void displaySelectedGeofenceOptions() {
        selecedGeofenceOptions.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSelectedGeofenceOptions() {
        selecedGeofenceOptions.setVisibility(View.GONE);
    }

    private void addMapFragment() {
        FragmentTransaction fragmentTransaction = activity
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.add(R.id.geofence_map_container, mapFragment);
        fragmentTransaction.commit();
    }

    private Flowable<LatLng> createLongPressEmitter() {
        return Flowable.create((FlowableOnSubscribe<LatLng>) emitter -> {
            googleMap.setOnMapLongClickListener(emitter::onNext);
            emitter.setCancellable(() -> googleMap.setOnMapLongClickListener(null));
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
