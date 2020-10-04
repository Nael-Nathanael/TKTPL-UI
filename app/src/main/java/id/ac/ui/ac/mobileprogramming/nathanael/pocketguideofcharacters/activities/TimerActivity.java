package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.service.ChronoService;

public class TimerActivity extends AppCompatActivity {

    Chronometer chronometer;
    FloatingActionButton start_countdown_button;
    FloatingActionButton pause_countdown_button;
    FloatingActionButton stop_countdown_button;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerActivityStartProtocol();
        timerActivityResumingProtocol();
        timerActivityButtonListener();
        timerActivityStopOldService();
    }

    private void timerActivityStopOldService() {
        Intent oldService = new Intent(TimerActivity.this, ChronoService.class);
        stopService(oldService);
    }

    private void timerActivityButtonListener() {
        start_countdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning()) {
                    long newbase = sharedPref.getLong("chronobase", SystemClock.elapsedRealtime());
                    long pauseOffset = sharedPref.getLong("chronopause", 0);
                    chronometer.setBase(newbase - pauseOffset);
                    chronometer.start();

                    sharedPref.edit()
                            .putBoolean("chronorun", true)
                            .putLong("chronobase", newbase - pauseOffset)
                            .apply();
                }
            }
        });

        pause_countdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning()) {
                    fullyStopTimer();

                    sharedPref.edit()
                            .putLong("chronopause", SystemClock.elapsedRealtime() - chronometer.getBase())
                            .putBoolean("chronorun", false)
                            .apply();
                }
            }
        });

        stop_countdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                fullyStopTimer();
            }
        });
    }

    private void resetTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    private void timerActivityResumingProtocol() {
        long newbase = sharedPref.getLong("chronobase", SystemClock.elapsedRealtime());
        long pauseOffset = sharedPref.getLong("chronopause", 0);
        chronometer.setBase(newbase - pauseOffset);

        if (sharedPref.getBoolean("chronorun", false)) {
            chronometer.start();
        }
    }

    private void timerActivityStartProtocol() {
        chronometer = findViewById(R.id.Chronometer);
        start_countdown_button = findViewById(R.id.start_countdown_button);
        stop_countdown_button = findViewById(R.id.stop_countdown_button);
        pause_countdown_button = findViewById(R.id.pause_countdown_button);
        sharedPref = TimerActivity.this.getSharedPreferences("chronosphere", Context.MODE_PRIVATE);
    }

    @Override
    protected void onStop() {
        if (isRunning()) {
            sharedPref.edit()
                    .putLong("chronobase", chronometer.getBase())
                    .putBoolean("chronorun", true)
                    .apply();

            Intent intent = new Intent(TimerActivity.this, ChronoService.class);
            startService(intent);
        }
        super.onStop();
    }

    private boolean isRunning() {
        return sharedPref.getBoolean("chronorun", false);
    }

    private void fullyStopTimer() {
        chronometer.stop();
        sharedPref.edit().clear().apply();
    }
}