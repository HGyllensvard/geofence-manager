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
import timber.log.Timber;

@FragmentWithArgs
public class ReactiveAlertDialogue extends DialogFragment {

    @Arg
    @StringRes
    int dialogMessageRes;

    @Arg
    @StringRes
    int positiveMessageRes;

    private AlertDialog.Builder builder;
    private Single<ReactiveDialogueResponse> mDialogResponse;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentArgs.inject(this);

        mDialogResponse = Single.create(emitter -> {
            builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(dialogMessageRes)
                    .setPositiveButton(positiveMessageRes, (dialog, id) -> {
                        emitter.onSuccess(ReactiveDialogueResponse.POSITIVE);
                    });
        });

        return builder.create();
    }

    public Single<ReactiveDialogueResponse> dialogResponse() {
        return mDialogResponse;
    }
}
