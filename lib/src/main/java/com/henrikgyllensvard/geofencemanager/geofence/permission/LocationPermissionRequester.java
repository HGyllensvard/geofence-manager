package com.henrikgyllensvard.geofencemanager.geofence.permission;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.henrikgyllensvard.geofencemanager.R;
import com.henrikgyllensvard.geofencemanager.dialogue.ReactiveAlertDialogue;
import com.henrikgyllensvard.geofencemanager.dialogue.ReactiveAlertDialogueBuilder;
import com.henrikgyllensvard.geofencemanager.dialogue.ReactiveDialogueResponse;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Single;


/**
 * NOTE: For now the usage is simple, and error cases are not managed.
 */
public class LocationPermissionRequester {

    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private AppCompatActivity activity;

    public LocationPermissionRequester(AppCompatActivity activity) {
        this.activity = activity;
    }

    public Single<RequestPermissionResult> request() {
        return Single.defer(() -> {
            int permissionCheck = ContextCompat.checkSelfPermission(activity,
                    ACCESS_FINE_LOCATION);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                return Single.just(RequestPermissionResult.GRANTED);
            } else {
                return informUserWhyPermissionNeeded()
                        .flatMap(dialogueResponse -> requestPermission());
            }
        });
    }

    private Single<Boolean> informUserWhyPermissionNeeded() {
        ReactiveAlertDialogue fragment = new ReactiveAlertDialogueBuilder(R.string.location, R.string.ok).build();
        fragment.show(activity.getSupportFragmentManager(), fragment.getTag());

        return fragment.dialogResponse()
                .map(reactiveDialogueResponse ->
                        reactiveDialogueResponse == ReactiveDialogueResponse.POSITIVE);
    }

    private Single<RequestPermissionResult> requestPermission() {
        return RxPermissions.getInstance(activity)
                .request(ACCESS_FINE_LOCATION)
                .singleOrError()
                .map(granted -> granted ? RequestPermissionResult.GRANTED : RequestPermissionResult.DENIED);
    }
}
