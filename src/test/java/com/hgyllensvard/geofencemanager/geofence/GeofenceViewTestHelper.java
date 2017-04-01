package com.hgyllensvard.geofencemanager.geofence;


import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceView;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceViewUpdate;

import java.util.Collections;
import java.util.List;

public class GeofenceViewTestHelper {

    public static final int FILL_COLOR = 1;
    public static final int STROKE_COLOR = 2;

    public static final GeofenceView GEOFENCE_VIEW_ONE = GeofenceView.create(GeofenceTestHelper.GEOFENCE_ONE, FILL_COLOR, STROKE_COLOR);
    public static final GeofenceView GEOFENCE_VIEW_TWO = GeofenceView.create(GeofenceTestHelper.GEOFENCE_TWO, FILL_COLOR, STROKE_COLOR);

    public static final List<GeofenceView> GEOFENCE_VIEWS = Collections.singletonList(GEOFENCE_VIEW_ONE);
    public static final List<GeofenceView> GEOFENCE_VIEWS_UPDATED = Collections.singletonList(GEOFENCE_VIEW_ONE);
    public static final List<GeofenceView> GEOFENCE_VIEWS_REMOVED = Collections.singletonList(GEOFENCE_VIEW_TWO);

    public static final GeofenceViewUpdate TEST_GEOFENCE_VIEW_UPDATE =
            GeofenceViewUpdate.create(GEOFENCE_VIEWS,
                    GEOFENCE_VIEWS_UPDATED,
                    GEOFENCE_VIEWS_REMOVED);
}
