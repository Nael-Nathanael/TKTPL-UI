package id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.R2;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.BgmPlayerService;
import id.ac.ui.cs.mobileprogramming.nathanael.pocketguideofcharacters.service.ChronoService;

/**
 * Create timer (count-up) activity with background rain and background music.
 *
 * @author Nathanael
 */
public class TimerActivity extends AppCompatActivity {

    /**
     * Primary component chronometer
     */
    @BindView(R2.id.Chronometer)
    Chronometer chronometer;

    /**
     * Control button to start chronometer
     */
    @BindView(R2.id.start_countdown_button)
    FloatingActionButton start_chrono_button;

    /**
     * Control button to pause chronometer
     */
    @BindView(R2.id.pause_countdown_button)
    FloatingActionButton pause_chrono_button;

    /**
     * Control button to stop chronometer
     */
    @BindView(R2.id.stop_countdown_button)
    FloatingActionButton stop_chrono_button;

    /**
     * Background video
     */
    @BindView(R2.id.rain_bg_view)
    VideoView videoview;

    /**
     * Background music
     */
    Intent bgmService;

    /**
     * Saved information on shared preferences
     */
    SharedPreferences sharedPref;

    /**
     * General step-by-step function to run.
     *
     * @param savedInstanceState to pass to superclass.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);

        timerActivityStartProtocol();
        timerActivityResumingProtocol();
        timerActivityButtonListener();
        timerActivityStopOldService();
    }

    /**
     * Function to control background video and background music.
     */
    private void setupBgm() {
        if (isRunning()) {
            videoview.setVisibility(View.VISIBLE);

            // set video's uri on another thread to prevent application detect not responding.
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

            // start background music service.
            startService(bgmService);
        } else {

            // stop video playback and hide video view
            videoview.stopPlayback();
            videoview.setVisibility(View.INVISIBLE);

            // stop background music service
            stopService(bgmService);
        }
    }

    /**
     * Stopping chrono service if ran beforehand.
     */
    private void timerActivityStopOldService() {
        Intent oldService = new Intent(TimerActivity.this, ChronoService.class);
        stopService(oldService);
    }


    /**
     * Bind control buttons to chrono and shared preference data modification
     */
    private void timerActivityButtonListener() {
        start_chrono_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only run this if stopwatch haven't already running
                if (!isRunning()) {

                    // get saved chronometer based or use now time as base
                    long newbase = sharedPref.getLong("chronobase", SystemClock.elapsedRealtime());

                    // get saved offset / passed time before this activity paused
                    long pauseOffset = sharedPref.getLong("chronopause", 0);

                    // set chronometer base and start
                    chronometer.setBase(newbase - pauseOffset);
                    chronometer.start();

                    // update chrono data in shared preference
                    sharedPref.edit()
                            .putBoolean("chronorun", true)
                            .putLong("chronobase", newbase - pauseOffset)
                            .apply();

                    // reconfigure background services
                    setupBgm();
                }
            }
        });

        pause_chrono_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // only run this if chronometer already running
                if (isRunning()) {
                    // stop timer
                    fullyStopTimer();

                    // save chronometer state and offset in shared preferences
                    sharedPref.edit()
                            .putLong("chronopause", SystemClock.elapsedRealtime() - chronometer.getBase())
                            .putBoolean("chronorun", false)
                            .apply();

                    // reconfigure background services
                    setupBgm();
                }
            }
        });

        stop_chrono_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // only run this if chronometer running
                if (isRunning()) {

                    // reset timer
                    resetTimer();

                    // stop timer
                    fullyStopTimer();

                    // reconfigure background services
                    setupBgm();
                }
            }
        });
    }


    /**
     * Reset current chronometer base to now time.
     */
    private void resetTimer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    /**
     * Check saved chronometer configuration at the beginning of this activity
     */
    private void timerActivityResumingProtocol() {
        long newbase = sharedPref.getLong("chronobase", SystemClock.elapsedRealtime());
        long pauseOffset = sharedPref.getLong("chronopause", 0);
        chronometer.setBase(newbase - pauseOffset);

        if (sharedPref.getBoolean("chronorun", false)) {
            chronometer.start();
        }
        setupBgm();
    }


    /**
     * Initialize variables and setup fullscreen
     */
    private void timerActivityStartProtocol() {
        sharedPref = TimerActivity.this.getSharedPreferences("chronosphere", Context.MODE_PRIVATE);

        // initialize audio/video service
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        bgmService = new Intent(TimerActivity.this, BgmPlayerService.class);
    }

    /**
     * Override stop state (back, home, service closed, app destroyed).
     */
    @Override
    protected void onStop() {
        // only run if chronometer in running state
        if (isRunning()) {

            // save current chronometer information to shared preferences
            sharedPref.edit()
                    .putLong("chronobase", chronometer.getBase())
                    .putBoolean("chronorun", true)
                    .apply();

            // start foreground service to remind user about this service.
            // Info: The BGM won't stop until timer stopped.
            Intent intent = new Intent(TimerActivity.this, ChronoService.class);
            startService(intent);
        }
        super.onStop();
    }

    /**
     * @return chronometer state from shared preferences
     */
    private boolean isRunning() {
        return sharedPref.getBoolean("chronorun", false);
    }

    /**
     * Stop chronometer and clear sharedPreferences configuration
     */
    private void fullyStopTimer() {
        chronometer.stop();
        sharedPref.edit().clear().apply();
    }
}