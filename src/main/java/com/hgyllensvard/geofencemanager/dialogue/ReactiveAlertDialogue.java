package com.hgyllensvard.geofencemanager.dialogue;


import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import io.reactivex.Single;


public class ReactiveAlertDialogue {

    private AlertDialog.Builder alertDialog;
    private Single<ReactiveDialogueResponse> dialogResponse;

    public ReactiveAlertDialogue(
            Context context,
            @StringRes int dialogMessageRes,
            @StringRes int positiveMessageRes) {

        dialogResponse = Single.create(emitter -> alertDialog = new AlertDialog.Builder(context)
                .setMessage(dialogMessageRes)
                .setPositiveButton(positiveMessageRes,
                        (dialog, which) -> {
                            if (!emitter.isDisposed()) {
                                emitter.onSuccess(ReactiveDialogueResponse.POSITIVE);
                            }
                        }));
    }

    public void show() {
        alertDialog.show();
    }

    public Single<ReactiveDialogueResponse> dialogueResponse() {
        return dialogResponse;
    }
}
