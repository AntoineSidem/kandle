package ch.epfl.sdp.kandle;


import android.content.res.Resources;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)


public class LoginActivityTest {

    Resources res = ApplicationProvider.getApplicationContext().getResources();

    @Rule
    public ActivityTestRule<LoginActivity> intentsRule =
            new ActivityTestRule<>(LoginActivity.class,true,true
            );




    @Test
    public void emptyEmailTest() {

        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.email)).check(matches(hasErrorText(res.getString(R.string.login_email_required))));

    }




    @Test
    public void emptyPasswordTest() throws InterruptedException {

        onView(withId(R.id.email)).perform(typeText("test@test.com"));
        onView(withId(R.id.email)).perform(closeSoftKeyboard());

        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.password)).check(matches(hasErrorText(res.getString(R.string.login_password_required))));
    }

    @Test
    public void wrongCredentialsTest() throws InterruptedException {

        onView(withId(R.id.email)).perform(typeText("zzzz@test.com"));
        onView(withId(R.id.email)).perform(closeSoftKeyboard());

        onView(withId(R.id.password)).perform(typeText("zzzzzzzzzz"));
        onView(withId(R.id.password)).perform(closeSoftKeyboard());

        onView(withId(R.id.loginBtn)).perform(click());

        //TODO
    }

    @Test
    public void authenticationTest() throws InterruptedException {

        onView(withId(R.id.email)).perform(typeText("anas.ibrahim@epfl.ch"));
        onView(withId(R.id.email)).perform(closeSoftKeyboard());

        onView(withId(R.id.password)).perform(typeText("12345678"));
        onView(withId(R.id.password)).perform(closeSoftKeyboard());

        //onView(withId(R.id.loginBtn)).perform(click());


        //intended(hasComponent(MainActivity.class.getName()));
        //onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        //onView(withId(R.id.navigation_view)).perform(NavigationViewActions.navigateTo(R.id.logout));

    }
    /*
    @Test
    public void alreadyHaveAnAccount() throws InterruptedException {

        onView(withId(R.id.signUpLink)).perform(click());
        intended(hasComponent(RegisterActivity.class.getName()));

    }
     */

}
