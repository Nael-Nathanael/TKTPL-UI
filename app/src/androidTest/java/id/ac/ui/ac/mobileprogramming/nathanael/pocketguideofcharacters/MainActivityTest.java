package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters;

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

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities.CreateCharacterActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters", appContext.getPackageName());
    }

    @Test
    public void whenLaunchedShouldShowRecyclerViewLayout() {
        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                View view = activity.findViewById(R.id.recyclerViewCard);
                assertNotNull(view);
            }
        });
    }

    @Test
    public void whenLaunchedHasFunctionalCreateFloatingButton() {
        onView(withId(R.id.addButton)).perform(click());

        final Activity[] currentActivity = {null};
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivities =
                        ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity[0] = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        assertEquals(CreateCharacterActivity.class, currentActivity[0].getClass());
    }
}