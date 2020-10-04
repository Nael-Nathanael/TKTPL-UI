package id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.R;
import id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.activities.TimerActivity;

import static id.ac.ui.ac.mobileprogramming.nathanael.pocketguideofcharacters.application.App.CHANNEL_ID;

public class ChronoService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent notificationIntent = new Intent(ChronoService.this, TimerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Pocket Guide of Characters")
                .setContentText("Your timer still running!")
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentIntent(pendingIntent)
                .setUsesChronometer(true)
                .build();

        startForeground(1, notification);

//        stopSelf();

        return START_NOT_STICKY;
    }
}
