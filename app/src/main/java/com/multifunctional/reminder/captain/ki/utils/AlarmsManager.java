package com.multifunctional.reminder.captain.ki.utils;

import android.content.Context;
import android.database.Cursor;

public class AlarmsManager {

    static AlarmsDB alarmsDB;

    public static boolean InsertAlarm(Context context, String name, String hours, String minutes, Boolean active) {
        if (alarmsDB == null) {
            alarmsDB = new AlarmsDB(context);
        }
        alarmsDB.InsertAlarm(name, hours, minutes, active);
        return true;
    }

    public static Cursor GetAllAlarms(Context context) {
        if (alarmsDB == null) {
            alarmsDB = new AlarmsDB(context);
        }
        return alarmsDB.GetAllAlarms();
    }

    public static void UpdateAlarm(Context context, int id, int hours, int minutes, String name, boolean active) {
        if (alarmsDB == null) {
            alarmsDB = new AlarmsDB(context);
        }

        alarmsDB.UpdateAlarm(id, hours, minutes, name, active);
    }


    public static void DeleteAlarm(Context context, int id) {
        if (alarmsDB == null) {
            alarmsDB = new AlarmsDB(context);
        }

        alarmsDB.DeleteAlarm(id);
    }

    public static Cursor GetAlarm(Context context, int id) {
        if (alarmsDB == null) {
            alarmsDB = new AlarmsDB(context);
        }
        return alarmsDB.GetAlarm(id);
    }

    public static Cursor GetAlarmsWithTime(Context context, int hours, int minutes) {
        if (alarmsDB == null) {
            alarmsDB = new AlarmsDB(context);
        }
        return alarmsDB.GetAlarmsWithTime(hours,minutes);
    }
}