package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.views.base.BaseActivity;

/**
 * Primary Activity only to create splash screen and redirect to landing activity
 *
 * @author Nathanael
 */
public class MainActivity extends AppCompatActivity {

    private final String VERSION = "Alpha 04";

    /**
     * @param savedInstanceState to pass to superclass
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.application_inner_version);
        textView.setText(VERSION);

        initSplashTimer();
    }

    /**
     * Create splash screen and redirect after 1000ms.
     */
    private void initSplashTimer() {
        int SPLASH_SCREEN_TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            Intent i = new Intent(MainActivity.this,
                    BaseActivity.class);
            startActivity(i);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}