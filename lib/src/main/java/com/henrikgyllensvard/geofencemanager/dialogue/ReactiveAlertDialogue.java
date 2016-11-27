package com.henrikgyllensvard.geofencemanager.dialogue;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;

@FragmentWithArgs
public class ReactiveAlertDialogue extends DialogFragment {

    @Arg
    @StringRes
    int dialogMessageRes;

    @Arg
    @StringRes
    int positiveMessageRes;

    private FlowableEmitter<ReactiveDialogueResponse> push;
    private Flowable<ReactiveDialogueResponse> mDialogResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);

        mDialogResponse = Flowable.create(emitter -> push = emitter,
                BackpressureStrategy.BUFFER);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogMessageRes)
                .setPositiveButton(positiveMessageRes, (dialog, id) -> {
                    if (!push.isCancelled()) {
                        push.onNext(ReactiveDialogueResponse.POSITIVE);
                    }
                });
        return builder.create();
    }

    public Single<ReactiveDialogueResponse> dialogResponse() {
        return mDialogResponse
                .singleOrError();
    }
}
