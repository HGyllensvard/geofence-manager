package com.hgyllensvard.geofencemanager;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ManageSingleGeofenceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manage_single_geofence_view, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Necessary to get access to dagger graph
        if (!(context instanceof GeofenceManagerInjector)) {
            throw new IllegalStateException("Context does not implement: %s" + GeofenceManagerInjector.class.getSimpleName());
        }
    }
}
