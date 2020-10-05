package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.BgmPlayerService;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.ChronoService;

public class TimerActivity extends AppCompatActivity {

    Chronometer chronometer;
    FloatingActionButton start_countdown_button;
    FloatingActionButton pause_countdown_button;
    FloatingActionButton stop_countdown_button;
    VideoView videoview;
    SharedPreferences sharedPref;
    Intent bgmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerActivityStartProtocol();
        timerActivityResumingProtocol();
        timerActivityButtonListener();
        timerActivityStopOldService();
    }

    private void setupBgm() {
        if (isRunning()) {
            videoview.setVisibility(View.VISIBLE);

            new Thread(new Runnable() {
                public void run() {
                    final Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rain);
                    videoview.post(new Runnable() {
                        public void run() {
                            videoview.setVideoURI(uri);
                            videoview.start();
                        }
                    });
                }
            }).start();
            startService(bgmService);
        } else {
            videoview.stopPlayback();
            videoview.setVisibility(View.INVISIBLE);
            stopService(bgmService);
        }
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
                    setupBgm();
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

                    setupBgm();
                }
            }
        });

        stop_countdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                fullyStopTimer();
                setupBgm();
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
        setupBgm();
    }

    private void timerActivityStartProtocol() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        chronometer = findViewById(R.id.Chronometer);
        start_countdown_button = findViewById(R.id.start_countdown_button);
        stop_countdown_button = findViewById(R.id.stop_countdown_button);
        pause_countdown_button = findViewById(R.id.pause_countdown_button);
        sharedPref = TimerActivity.this.getSharedPreferences("chronosphere", Context.MODE_PRIVATE);
        videoview = findViewById(R.id.rain_bg_view);

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        bgmService = new Intent(TimerActivity.this, BgmPlayerService.class);
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