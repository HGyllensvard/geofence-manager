package com.hgyllensvard.geofencemanager;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hgyllensvard.geofencemanager.buildingBlocks.di.ActivityModule;
import com.hgyllensvard.geofencemanager.di.GeofenceModuleManager;
import com.hgyllensvard.geofencemanager.geofence.di.GeofenceManagerComponent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@FragmentWithArgs
public class SingleGeofenceFragment extends Fragment implements GeofenceManagerInjector {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;

    private GeofenceManagerComponent geofenceManagerComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentArgs.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_single_geofence_view, null);

        unbinder = ButterKnife.bind(this, view);

        setupToolbar();

        return view;
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbar.setNavigationOnClickListener(view -> getActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AppCompatActivity) {
            if (context instanceof GeofenceManagerInjector) {
                geofenceManagerComponent = ((GeofenceManagerInjector) getActivity()).getGeofenceManagerComponent();
            } else {
                geofenceManagerComponent = GeofenceModuleManager.geofenceManagerComponent(getContext())
                        .plus(new ActivityModule((AppCompatActivity) getActivity()),
                                new GeofenceManagerModule());
            }
        } else {
            throw new IllegalStateException("Activity is not an AppCompatActivity");
        }
    }

    @Override
    public GeofenceManagerComponent getGeofenceManagerComponent() {
        return geofenceManagerComponent;
    }
}
