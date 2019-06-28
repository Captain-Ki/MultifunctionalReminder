package com.multifunctional.reminder.captain.ki.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.provider.Settings;

import com.multifunctional.reminder.captain.ki.MainActivity;
import com.multifunctional.reminder.captain.ki.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmAlertService extends Service {
    private Timer timer = new Timer();

    Cursor result;

    public boolean isServiceRunning;


    public AlarmAlertService() {
        isServiceRunning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        isServiceRunning = true;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CheckAlarms();
            }
        }, 0, 1000 * 30);

        return super.onStartCommand(intent, flags, startId);
    }


    private void stopService() {
        isServiceRunning = false;
        timer.cancel();
        stopSelf();

    }

    private void CheckAlarms() {

        int hours = 0;
        int minutes = 0;
        String name = "";

        SimpleDateFormat sdfH = new SimpleDateFormat("HH");
        SimpleDateFormat sdfM = new SimpleDateFormat("mm");

        hours = Integer.parseInt(sdfH.format(new Date()));
        minutes = Integer.parseInt(sdfM.format(new Date()));

        result = AlarmsManager.GetAlarmsWithTime(getApplicationContext(), hours, minutes);

        if (result != null) {
            if (result.getCount() >= 1) {
                if (result.moveToFirst()) {
                    if (result.getString(result.getColumnIndex(AlarmsDB.ALARMS_COLUMN_ACTIVE)).equals("1")) {
                        do {
                            name = result.getString(result.getColumnIndex(AlarmsDB.ALARMS_COLUMN_NAME));
                            if(!name.equals("")){
                                PlayAlarm(name);
                                return;
                            }
                        } while (result.moveToNext());
                    }
                }
            }
        }
        result.close();
    }

    private void PlayAlarm(String name) {

        int mNotificationId = 002;
        long[] pattern = {500,500,500,500,500,500,500,500,500,500};

        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_alarm_notification)
                        .setContentTitle(name)
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
    }
}
