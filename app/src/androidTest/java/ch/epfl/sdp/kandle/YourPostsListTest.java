package ch.epfl.sdp.kandle;
import android.view.Gravity;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import ch.epfl.sdp.kandle.dependencies.DependencyManager;
import ch.epfl.sdp.kandle.fragment.YourPostListFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class YourPostsListTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true) {
                @Override
                protected void beforeActivityLaunched() {
                    DependencyManager.setFreshTestDependencies(true);
                }
            };

    @Before
    public void loadPostView() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.navigation_view)).perform(NavigationViewActions.navigateTo(R.id.your_posts));
    }

    @Test
    public void canClickOnPostAndRemoveIt() throws Throwable {


        this.mainActivityRule.runOnUiThread(() -> {
            YourPostListFragment frag = (YourPostListFragment) mainActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent);
            Post p =  new Post("Text", "( : this is my post : )", new Date(),"id");
            frag.putInPostList(p);
        });

        onView(withId(R.id.rvPosts)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));


        onView(withId(R.id.post_content)).perform(click());




    }

    @Test
    public void putTwoNewPostsIntoTheRecyclerAndDeleteThem() throws Throwable {
        this.mainActivityRule.runOnUiThread(() -> {
            YourPostListFragment frag = (YourPostListFragment) mainActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent);
            Post p =  new Post("Text", "( : this is my post 1 : )", new Date(),"id");
            Post p1 =  new Post("Text", "( : this is my post 2 : )", new Date(),"id");
            frag.putInPostList(p);
            frag.putInPostList(p1);
            frag.removePostAtIndex(0);
            frag.removePost(p1);
        });
    }

    @Test
    public void addTwoPostAndLikeThenDislikeThemBoth() throws Throwable {
        YourPostListFragment frag = YourPostListFragment.newInstance();
        frag.getPostList();
        this.mainActivityRule.runOnUiThread(() -> {
            YourPostListFragment frag1 = (YourPostListFragment) mainActivityRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent);
            Post p =  new Post("Text", "( : this is my post : )", new Date(),"id");
            Post p1 =  new Post("Text", "( : this is my post : )", new Date(),"id");
            frag1.putInPostList(p);
            frag1.putInPostList(p1);


        });
        onView(withId(R.id.rvPosts)).perform(RecyclerViewActions.actionOnItemAtPosition(1,clickChildViewWithId(R.id.likeButton)));
        onView(withId(R.id.rvPosts)).perform(RecyclerViewActions.actionOnItemAtPosition(0,clickChildViewWithId(R.id.likeButton)));
        onView(withId(R.id.rvPosts)).perform(RecyclerViewActions.actionOnItemAtPosition(1,clickChildViewWithId(R.id.likeButton)));
        onView(withId(R.id.rvPosts)).perform(RecyclerViewActions.actionOnItemAtPosition(0,clickChildViewWithId(R.id.likeButton)));
    }





    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();

            }

        };
    }







}