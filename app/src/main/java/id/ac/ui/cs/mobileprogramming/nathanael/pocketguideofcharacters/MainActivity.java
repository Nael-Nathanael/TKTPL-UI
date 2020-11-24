package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities.LandingActivity;

/**
 * Primary Activity only to create splash screen and redirect to landing activity
 * @author Nathanael
 */
public class MainActivity extends AppCompatActivity {

    /**
     * @param savedInstanceState to pass to superclass
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_main);
        initSplashTimer();
    }

    /**
     * Create splash screen and redirect after 1000ms.
     */
    private void initSplashTimer() {
        int SPLASH_SCREEN_TIME_OUT = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this,
                        LandingActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    /**
     * Set activity as fullscreen: flag it and hide support action bar.
     */
    private void setFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }
}