package com.hgyllensvard.geofencemanager.toolbar;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.hgyllensvard.geofencemanager.GeofenceManagerInjector;
import com.hgyllensvard.geofencemanager.R;
import com.hgyllensvard.geofencemanager.R2;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;

public class EditableTitleEditableToolbar extends android.support.v7.widget.Toolbar implements EditableToolbarView {

    @Inject
    EditableTitleToolbarPresenter titleToolbarPresenter;

    private Unbinder unbinder;

    @BindView(R2.id.toolbar_title)
    EditText toolbarTitle;

    private Observable<String> titleObservable;

    public EditableTitleEditableToolbar(Context context) {
        super(context);
    }

    public EditableTitleEditableToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditableTitleEditableToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        inflate(getContext(), R.layout.toolbar_view, this);
        unbinder = ButterKnife.bind(this);

        titleObservable = RxJavaInterop.toV2Observable(
                RxTextView.afterTextChangeEvents(toolbarTitle)
                        .map(TextViewAfterTextChangeEvent::editable)
                        .filter(editable -> editable != null)
                        .map(CharSequence::toString));

        injectDependencies();

        titleToolbarPresenter.bindView(this);
    }

    @Override
    public Observable<String> observeTitle() {
        return titleObservable;
    }

    @Override
    public ToolbarTitle title() {
        return ToolbarTitle.create(toolbarTitle.getText().toString());
    }

    @Override
    public void title(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        titleToolbarPresenter.unbindView();
        unbinder.unbind();
    }

    private void injectDependencies() {
        if (getContext() instanceof GeofenceManagerInjector) {
            ((GeofenceManagerInjector) getContext()).getGeofenceManagerComponent()
                    .inject(this);
        } else {
            throw new IllegalStateException("Context does not implement: %s" + GeofenceManagerInjector.class.getSimpleName());
        }
    }
}
