package ch.epfl.sdp.kandle;

import android.view.Gravity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.rule.ActivityTestRule;

import java.util.HashMap;
import java.util.LinkedList;
import ch.epfl.sdp.kandle.dependencies.DependencyManager;
import ch.epfl.sdp.kandle.dependencies.MockAuthentication;
import ch.epfl.sdp.kandle.dependencies.MockDatabase;
import ch.epfl.sdp.kandle.dependencies.MockStorage;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class SettingsFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> intentsRule =
            new ActivityTestRule<MainActivity>(MainActivity.class,true,true
            ){
                @Override
                protected void beforeActivityLaunched() {
                    LoggedInUser.init(new User("loggedInUserId","LoggedInUser","loggedInUser@kandle.ch","nickname","image"));
                    HashMap<String, String> accounts = new HashMap<>();
                    HashMap<String,User> users = new HashMap<>();
                    HashMap<String, MockDatabase.Follow> followMap = new HashMap<>();
                    HashMap<String,Post> posts = new HashMap<>();
                    MockDatabase db = new MockDatabase(true, users, followMap, posts);
                    MockAuthentication authentication = new MockAuthentication(true, accounts, "password");
                    MockStorage storage = new MockStorage();
                    DependencyManager.setFreshTestDependencies(authentication, db, storage);


                }
            };






    @After
    public void clearCurrentUser(){
        LoggedInUser.clear();
    }


    @Before
    public void loadFragment() throws InterruptedException {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.navigation_view)).perform(NavigationViewActions.navigateTo(R.id.settings));
        Thread.sleep(1000);
    }


    @Test
    public void wrongOldPasswordDisplaysError() throws InterruptedException {
        onView(withId(R.id.modifyPassword)).perform(click());
        onView(withId(R.id.oldPassword)).perform(typeText("passworf"));
        onView(withId(R.id.oldPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.validatePasswordButton)).perform(click());
        onView(withId(R.id.oldPassword)).check(matches(hasErrorText("Unable to authenticate, please check that your password is correct")));
    }

    @Test
    public void invalidNewPasswordDisplaysError() throws InterruptedException {
        onView(withId(R.id.modifyPassword)).perform(click());
        onView(withId(R.id.oldPassword)).perform(typeText("password"));
        onView(withId(R.id.newPassword)).perform(typeText("passwor"));
        onView(withId(R.id.newPassword)).perform(closeSoftKeyboard());
        onView(withId(R.id.validatePasswordButton)).perform(click());
        onView(withId(R.id.newPassword)).check(matches(hasErrorText("Please choose a password of more than 8 characters !")));
    }

    @Test
    public void notMatchingPasswordsDisplaysError() throws InterruptedException {
        onView(withId(R.id.modifyPassword)).perform(click());
        onView(withId(R.id.oldPassword)).perform(typeText("password"));
        onView(withId(R.id.newPassword)).perform(typeText("HoldTheDoor"));
        onView(withId(R.id.newPasswordConfirm)).perform(typeText("PasAuDehors"));
        onView(withId(R.id.newPasswordConfirm)).perform(closeSoftKeyboard());
        onView(withId(R.id.validatePasswordButton)).perform(click());
        onView(withId(R.id.newPasswordConfirm)).check(matches(hasErrorText("Your passwords do not match !")));
    }

    @Test
    public void correctPasswordInputDisplaysToast() throws InterruptedException {
        onView(withId(R.id.modifyPassword)).perform(click());
        onView(withId(R.id.oldPassword)).perform(typeText("password"));
        onView(withId(R.id.newPassword)).perform(typeText("newpassword"));
        onView(withId(R.id.newPasswordConfirm)).perform(typeText("newpassword"));
        onView(withId(R.id.newPasswordConfirm)).perform(closeSoftKeyboard());
        onView(withId(R.id.validatePasswordButton)).perform(click());
        onView(withText("Your password has been succesfully updated")).inRoot(withDecorView(not(is(intentsRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void viewsCanBeExpanded() {
        onView(withId(R.id.otherSettings)).perform(click());
        onView(withId(R.id.otherSettings)).perform(click());
    }



}
