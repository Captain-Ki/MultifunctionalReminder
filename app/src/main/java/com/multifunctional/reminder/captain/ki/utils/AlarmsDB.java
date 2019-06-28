package com.multifunctional.reminder.captain.ki.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmsDB extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "MULTIFUNCTIONALREMINDER";
    public static final String ALARMS_TABLE_NAME = "ALARMS";
    public static final String ALARMS_COLUMN_ID = "id";
    public static final String ALARMS_COLUMN_NAME = "name";
    public static final String ALARMS_COLUMN_HOURS = "hours";
    public static final String ALARMS_COLUMN_MINUTES = "minutes";
    public static final String ALARMS_COLUMN_ACTIVE = "active";


    public AlarmsDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ALARMS_TABLE_NAME + "(id integer primary key, name text, hours text, minutes text, active text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    boolean InsertAlarm(String name, String hours, String minutes, Boolean active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("hours", hours);
        contentValues.put("minutes", minutes);
        contentValues.put("active", active);
        db.insert(ALARMS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    Cursor GetAllAlarms() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + ALARMS_TABLE_NAME, null);
        return result;
    }

    public void UpdateAlarm(int id, int hours, int minutes, String name, boolean active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ALARMS_COLUMN_NAME, name);
        contentValues.put(ALARMS_COLUMN_HOURS, hours);
        contentValues.put(ALARMS_COLUMN_MINUTES, minutes);
        contentValues.put(ALARMS_COLUMN_ACTIVE, active);


        db.update(ALARMS_TABLE_NAME, contentValues, "id = ?",
                new String[]{String.valueOf(id)});
    }


    public void DeleteAlarm(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ALARMS_TABLE_NAME, "id = " + id, null);

        db.close();
    }

    Cursor GetAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + ALARMS_TABLE_NAME + " where id= " + id, null);

        return result;
    }

    Cursor GetAlarmsWithTime(int hours, int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + ALARMS_TABLE_NAME + " where hours= " + hours + " and minutes= " + minutes, null);

        return result;
    }
}
