package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.CreateCharacterActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CreateActivityTest {

    @Rule
    public ActivityScenarioRule<CreateCharacterActivity> activityScenarioRule = new ActivityScenarioRule<>(CreateCharacterActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters", appContext.getPackageName());
    }

    @Test
    public void userCanEnterName() {
        onView(withId(R.id.nameField)).perform(typeText("Nathanael"));
        onView(withId(R.id.nameField)).check(matches(withText("Nathanael")));
    }

    @Test
    public void userCanEnterAge() {
        onView(withId(R.id.ageField)).perform(typeText("12"));
        onView(withId(R.id.ageField)).check(matches(withText("12")));
    }
}
