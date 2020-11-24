package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.LandingActivity;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class LandingActivityTest {

    @Rule
    public ActivityScenarioRule<LandingActivity> activityScenarioRule = new ActivityScenarioRule<>(LandingActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters", appContext.getPackageName());
    }

    @Test
    public void whenLaunchedShouldShowRecyclerViewLayout() {
        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<LandingActivity>() {
            @Override
            public void perform(LandingActivity activity) {
                View view = activity.findViewById(R.id.recyclerViewCard);
                assertNotNull(view);
            }
        });
    }
}