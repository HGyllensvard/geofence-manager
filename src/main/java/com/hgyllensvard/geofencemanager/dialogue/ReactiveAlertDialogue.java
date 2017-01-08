package com.hgyllensvard.geofencemanager.dialogue;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

@FragmentWithArgs
public class ReactiveAlertDialogue extends DialogFragment {

    @Arg
    @StringRes
    int dialogMessageRes;

    @Arg
    @StringRes
    int positiveMessageRes;

    private SingleEmitter<ReactiveDialogueResponse> push;
    private Single<ReactiveDialogueResponse> mDialogResponse;

    public ReactiveAlertDialogue() {
        mDialogResponse = Single.create(emitter -> push = emitter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentArgs.inject(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogMessageRes)
                .setPositiveButton(positiveMessageRes, (dialog, id) -> {
                    if (!push.isDisposed()) {
                        push.onSuccess(ReactiveDialogueResponse.POSITIVE);
                    }
                });
        return builder.create();
    }

    public Single<ReactiveDialogueResponse> dialogResponse() {
        return mDialogResponse;
    }
}