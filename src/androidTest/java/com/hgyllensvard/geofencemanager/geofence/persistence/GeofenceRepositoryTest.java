package com.hgyllensvard.geofencemanager.geofence.persistence;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;
import com.hgyllensvard.geofencemanager.geofence.GeofenceData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.subscribers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * As Realm requires a Looper, we sadly need to run the actual tests of the database
 * on a real device.
 */
@RunWith(AndroidJUnit4.class)
public class GeofenceRepositoryTest {

    @Rule
    ActivityTestRule<RealmActivityTest> activityTestRule = new ActivityTestRule<>(RealmActivityTest.class);

    private GeofenceRepository geofenceRepository;

    @Before
    public void setup() {
        geofenceRepository = activityTestRule.getActivity().geofenceRepository;
    }

    @Test
    public void shouldManageToSaveAndGetGeofence() throws Exception {

        String name = "Some Geofence";
        double lat = 10d;
        double lng = 20d;
        int radius = 1;

        LatLng latLng = new LatLng(lat, lng);

        TestSubscriber<List<GeofenceData>> testSubscriber = geofenceRepository.listenGeofences().test(1);

        geofenceRepository.save(name, latLng, radius);

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValueAt(1, geofenceDatas -> {
            assertThat(geofenceDatas).hasSize(1);
            GeofenceData data = geofenceDatas.get(0);

            return data.name().equals(name) &&
                    data.latLng().latitude == lat &&
                    data.latLng().longitude == lng &&
                    data.radius() == radius;
        });
    }
}