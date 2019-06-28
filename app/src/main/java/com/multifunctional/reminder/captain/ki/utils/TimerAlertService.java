package com.multifunctional.reminder.captain.ki.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;

import com.multifunctional.reminder.captain.ki.MainActivity;
import com.multifunctional.reminder.captain.ki.R;

public class TimerAlertService extends Service {


    public TimerAlertService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ShowAlert();

        return super.onStartCommand(intent, flags, startId);
    }

    private void stopService() {
        stopSelf();
    }

    private void ShowAlert() {

        int mNotificationId = 001;
        long[] pattern = {500,500,500,500,500,500,500,500,500,500};


        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_check_white_24dp)
                        .setContentTitle("Time is up!")
                        .setContentText(" ")
                        .setOngoing(false)
                        .setAutoCancel(true)
                        .setVibrate(pattern)
                        .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(mNotificationId, mBuilder.build());

        stopService();
    }
}
