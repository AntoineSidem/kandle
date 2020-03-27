package ch.epfl.sdp.kandle;

import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sdp.kandle.fragment.MapFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapTest {
    @Rule
    public final ActivityTestRule<MainActivity> mainActivityRule =
            new ActivityTestRule<>(MainActivity.class);


    private GoogleMap googleMap;
    private CountingIdlingResource countingIdlingResource = new CountingIdlingResource("MapReady");
    private SupportMapFragment mapFragment;


    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Before
    public void beforeEach() {
        countingIdlingResource.increment();

        mapFragment = (SupportMapFragment) mainActivityRule.getActivity().getCurrentFragment().getChildFragmentManager().findFragmentById(R.id.map_support);
        final OnMapReadyCallback onMapReadyCallback = googleMap -> {
            countingIdlingResource.decrement();

            MapTest.this.googleMap = googleMap;
        };

    }

}
