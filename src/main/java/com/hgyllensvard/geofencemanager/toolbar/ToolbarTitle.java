package com.hgyllensvard.geofencemanager.toolbar;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ToolbarTitle {

    public abstract String title();

    public static ToolbarTitle create(String title) {
        return new AutoValue_ToolbarTitle(title);
    }
}
