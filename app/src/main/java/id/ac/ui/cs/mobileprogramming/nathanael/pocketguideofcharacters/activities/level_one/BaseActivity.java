package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.level_one;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.NavigationViewModel;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.TimerActivity;

public class BaseActivity extends AppCompatActivity {

    NavigationViewModel navigationViewModel;
    ViewPager2 viewPager2;
    /**
     * Required attribute to override exit by pressing back button twice.
     * Ref: https://stackoverflow.com/a/20853151/13645004.
     */
    private boolean backPressedToExitOnce = false;
    /**
     * Required attribute to show toast when overriding exit by pressing back button twice.
     * Ref: https://stackoverflow.com/a/20853151/13645004.
     */
    private Toast toast = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        viewPager2 = findViewById(R.id.base_pager);
        viewPager2.setAdapter(new BasePagerAdapter(this));


        navigationViewModel = new ViewModelProvider(this).get(NavigationViewModel.class);

        // Create the observer
        final Observer<Boolean> stateObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean active) {
                Log.d("NaelsObserver", String.valueOf(active));
                viewPager2.setUserInputEnabled(active);
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        navigationViewModel.getActive().observe(this, stateObserver);
    }

    /**
     * Alternative to OnBackButtonPressed that does not work without explored reason.
     *
     * @param keyCode pressed key
     * @param event   to pass to superclass
     * @return superclass return value. true if default back allowed, false otherwise.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // check back key pressed
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // name current visible fragment is Chat
            if (viewPager2.getCurrentItem() == 1) {

                // automatically go back to card list fragment
                viewPager2.setCurrentItem(0);

                // do not return default back, it will break the home fragment.
                return false;
            } else if (!backPressedToExitOnce) {

                // require user to press back button twice in 2000ms to exit;
                this.backPressedToExitOnce = true;

                // show toast
                showToast("Press again to exit");

                // start 2000ms counter
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressedToExitOnce = false;
                    }
                }, 2000);

                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Created to make sure that you toast doesn't show multiple times, if user pressed back
     * button more than once. Ref: https://stackoverflow.com/a/20853151/13645004.
     *
     * @param message Message to show on toast.
     */
    private void showToast(String message) {
        if (this.toast == null)
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        else if (this.toast.getView() == null)
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        else this.toast.setText(message);
        this.toast.show();
    }

    /**
     * Kill the toast if showing. Supposed to call from onPause() of activity.
     * So that toast also get removed as activity goes to background, to improve
     * better app experience for user. Ref: https://stackoverflow.com/a/20853151/13645004.
     */
    private void killToast() {
        if (this.toast != null) {
            this.toast.cancel();
        }
    }

    /**
     * Override on pause to kill toast before pausing.
     */
    @Override
    public void onPause() {
        killToast();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_timer_menu_with_chat, menu);
        return true;
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.timerModeButton) {
            startActivity(
                    new Intent(this, TimerActivity.class)
            );
        } else if (id == R.id.chatModeButton) {
            viewPager2.setCurrentItem(1);
        } else if (id == android.R.id.home) {
            viewPager2.setCurrentItem(0);
        }
        return super.onOptionsItemSelected(item);
    }
}
