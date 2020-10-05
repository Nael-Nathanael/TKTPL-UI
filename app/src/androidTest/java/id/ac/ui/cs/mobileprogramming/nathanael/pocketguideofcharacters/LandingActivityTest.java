package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.BottomNavigationActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class LandingActivityTest {

    @Rule
    public ActivityScenarioRule<BottomNavigationActivity> activityScenarioRule = new ActivityScenarioRule<>(BottomNavigationActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters", appContext.getPackageName());
    }

    @Test
    public void whenLaunchedShouldShowRecyclerViewLayout() {
        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<BottomNavigationActivity>() {
            @Override
            public void perform(BottomNavigationActivity activity) {
                View view = activity.findViewById(R.id.recyclerViewCard);
                assertNotNull(view);
            }
        });
    }
}