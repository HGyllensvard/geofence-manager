package com.hgyllensvard.geofencemanager.geofence.permission;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.dialogue.ReactiveAlertDialogue;
import com.hgyllensvard.geofencemanager.dialogue.ReactiveDialogueResponse;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Single;


/**
 * NOTE: For now the usage is simple, and error cases are not managed.
 */
public class LocationPermissionRequester {

    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    private Context context;

    public LocationPermissionRequester(Context context) {
        this.context = context;
    }

    public Single<RequestPermissionResult> request() {
        return Single.defer(() -> {
            int permissionCheck = ContextCompat.checkSelfPermission(context,
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
        ReactiveAlertDialogue fragment = new ReactiveAlertDialogue(context, R.string.location, R.string.ok);
        fragment.show();

        return fragment.dialogueResponse()
                .map(reactiveDialogueResponse ->
                        reactiveDialogueResponse == ReactiveDialogueResponse.POSITIVE);
    }

    private Single<RequestPermissionResult> requestPermission() {
        return RxPermissions.getInstance(context)
                .request(ACCESS_FINE_LOCATION)
                .singleOrError()
                .map(granted -> granted ? RequestPermissionResult.GRANTED : RequestPermissionResult.DENIED);
    }
}
