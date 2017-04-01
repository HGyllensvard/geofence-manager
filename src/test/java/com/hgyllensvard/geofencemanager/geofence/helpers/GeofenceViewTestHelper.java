package com.hgyllensvard.geofencemanager.geofence.helpers;


import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceView;
import com.hgyllensvard.geofencemanager.geofence.edit.map.GeofenceViewUpdate;

import java.util.Collections;
import java.util.List;

import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceMapOptionsTestHelper.FILL_COLOUR;
import static com.hgyllensvard.geofencemanager.geofence.helpers.GeofenceMapOptionsTestHelper.STROKE_COLOUR;

public class GeofenceViewTestHelper {

    public static final GeofenceView GEOFENCE_VIEW_ONE = GeofenceView.create(GeofenceTestHelper.GEOFENCE_ONE, FILL_COLOUR, STROKE_COLOUR);
    public static final GeofenceView GEOFENCE_VIEW_TWO = GeofenceView.create(GeofenceTestHelper.GEOFENCE_TWO, FILL_COLOUR, STROKE_COLOUR);

    public static final List<GeofenceView> GEOFENCE_VIEWS = Collections.singletonList(GEOFENCE_VIEW_ONE);
    public static final List<GeofenceView> GEOFENCE_VIEWS_UPDATED = Collections.singletonList(GEOFENCE_VIEW_ONE);
    public static final List<GeofenceView> GEOFENCE_VIEWS_REMOVED = Collections.singletonList(GEOFENCE_VIEW_TWO);

    public static final GeofenceViewUpdate TEST_GEOFENCE_VIEW_UPDATE =
            GeofenceViewUpdate.create(GEOFENCE_VIEWS,
                    GEOFENCE_VIEWS_UPDATED,
                    GEOFENCE_VIEWS_REMOVED);
}
