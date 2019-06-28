package com.multifunctional.reminder.captain.ki.fragments;


import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.multifunctional.reminder.captain.ki.R;
import com.multifunctional.reminder.captain.ki.adapters.AlarmRecyclerViewAdapter;
import com.multifunctional.reminder.captain.ki.utils.AlarmsDB;
import com.multifunctional.reminder.captain.ki.utils.AlarmsManager;

import java.util.ArrayList;

import one.xcorp.widget.swipepicker.SwipePicker;

public class AlarmFragment extends Fragment implements AlarmRecyclerViewAdapter.ItemClickListener {

    View view;

    ImageView showCreateAlarmDialogButton;

    Dialog createAlarmDialog, updateAlarmDialog;

    AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;

    ArrayList<Integer> id;
    ArrayList<String> names;
    ArrayList<String> hours;
    ArrayList<String> minutes;
    ArrayList<String> active;

    RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_alarm, container, false);

        showCreateAlarmDialogButton = view.findViewById(R.id.show_create_alarm_dialog_button);
        showCreateAlarmDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCreateAlarmDilog();
            }
        });

        recyclerView = view.findViewById(R.id.alarms_recycler_view);

        GetAlarms();
        updateAlarmsList();

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static AlarmFragment newInstance() {
        AlarmFragment fragment1 = new AlarmFragment();
        Bundle args = new Bundle();
        fragment1.setArguments(args);
        return fragment1;
    }


    private void ShowCreateAlarmDilog() {

        createAlarmDialog = new Dialog(getContext());
        createAlarmDialog.setContentView(R.layout.create_alarm_dialog);

        final SwipePicker alarmMinutes = createAlarmDialog.findViewById(R.id.alarm_minutes_picker);
        final SwipePicker alarmHours = createAlarmDialog.findViewById(R.id.alarm_hours_picker);

        final EditText alarmName = createAlarmDialog.findViewById(R.id.create_alarm_name);
        Button createAlarm = createAlarmDialog.findViewById(R.id.create_dialog_button);
        Button cancelCreateDialog = createAlarmDialog.findViewById(R.id.cancel_create_alarm_dialog_button);

        final TextView invalidDataText = createAlarmDialog.findViewById(R.id.create_alarm_dialog_invalid_text);
        invalidDataText.setVisibility(View.INVISIBLE);

        alarmMinutes.setHintTextColor(getResources().getColor(R.color.green_hint));
        alarmHours.setHintTextColor(getResources().getColor(R.color.green_hint));

        cancelCreateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmMinutes.setValue(0);
                alarmHours.setValue(0);

                alarmName.setText(" ");

                invalidDataText.setVisibility(View.INVISIBLE);

                createAlarmDialog.dismiss();
            }
        });

        createAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((alarmHours.getValue() >= 0) && (alarmMinutes.getValue() >= 0) && alarmName.getText().length() >= 1) {

                    String name = alarmName.getText().toString();

                    String minutes = String.valueOf((int) alarmMinutes.getValue());

                    String hours = String.valueOf((int) alarmHours.getValue());

                    AlarmsManager.InsertAlarm(getContext(), name, hours, minutes, true);

                    alarmMinutes.setValue(0);
                    alarmHours.setValue(0);

                    alarmName.setText(" ");

                    invalidDataText.setVisibility(View.INVISIBLE);

                    createAlarmDialog.dismiss();

                    Toast.makeText(getContext(), "Alarm successfully created", Toast.LENGTH_SHORT).show();

                    GetAlarms();
                    updateAlarmsList();


                } else {
                    invalidDataText.setVisibility(View.VISIBLE);
                }
            }
        });

        createAlarmDialog.show();
    }


    private void GetAlarms() {


        id = new ArrayList<>();
        names = new ArrayList<>();
        hours = new ArrayList<>();
        minutes = new ArrayList<>();
        active = new ArrayList<>();

        Cursor cursor = AlarmsManager.GetAllAlarms(getContext());

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    id.add(cursor.getInt(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_ID)));
                    names.add(cursor.getString(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_NAME)));
                    hours.add(cursor.getString(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_HOURS)));
                    minutes.add(cursor.getString(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_MINUTES)));
                    active.add(cursor.getString(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_ACTIVE)));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void updateAlarmsList() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(getContext(), id, hours, minutes, names, active);
        alarmRecyclerViewAdapter.setClickListener(this);
        recyclerView.setAdapter(alarmRecyclerViewAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        ShowUpdateAlarmDilog(alarmRecyclerViewAdapter.getItem(position));
    }

    private void ShowUpdateAlarmDilog(final Integer id) {

        Cursor cursor = AlarmsManager.GetAlarm(getContext(), id);

        int oldHours = 0, oldMinutes = 0;
        String oldName = "";
        boolean oldActive = true;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    oldHours = cursor.getInt(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_HOURS));
                    oldMinutes = cursor.getInt(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_MINUTES));
                    oldName = cursor.getString(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_NAME));

                    if (cursor.getString(cursor.getColumnIndex(AlarmsDB.ALARMS_COLUMN_ACTIVE)).equals("1")) {
                        oldActive = true;
                    } else {
                        oldActive = false;
                    }
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        updateAlarmDialog = new Dialog(getContext());
        updateAlarmDialog.setContentView(R.layout.update_alarm_dialog);

        final SwipePicker alarmMinutes = updateAlarmDialog.findViewById(R.id.update_alarm_minutes_picker);
        final SwipePicker alarmHours = updateAlarmDialog.findViewById(R.id.update_alarm_hours_picker);
        final Switch activeUpdateDialog = updateAlarmDialog.findViewById(R.id.update_alarm_active);

        final EditText alarmName = updateAlarmDialog.findViewById(R.id.update_alarm_name);
        Button updateAlarm = updateAlarmDialog.findViewById(R.id.update_dialog_button);
        Button cancelUpdateDialog = updateAlarmDialog.findViewById(R.id.cancel_update_alarm_dialog_button);
        Button deleteUpdateDialog = updateAlarmDialog.findViewById(R.id.delete_update_alarm_dialog_button);

        final TextView invalidDataText = updateAlarmDialog.findViewById(R.id.update_alarm_dialog_invalid_text);
        invalidDataText.setVisibility(View.INVISIBLE);

        alarmMinutes.setHintTextColor(getResources().getColor(R.color.green_hint));
        alarmHours.setHintTextColor(getResources().getColor(R.color.green_hint));

        alarmName.setText(oldName);
        alarmHours.setValue(oldHours);
        alarmMinutes.setValue(oldMinutes);
        alarmHours.setHint(String.valueOf(oldHours));
        alarmMinutes.setHint(String.valueOf(oldMinutes));
        activeUpdateDialog.setChecked(oldActive);

        updateAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((alarmHours.getValue() >= 0) && (alarmMinutes.getValue() >= 0) && alarmName.getText().length() >= 1) {

                    String name = alarmName.getText().toString();

                    int hours = (int) alarmHours.getValue();

                    int minutes = (int) alarmMinutes.getValue();
                    boolean active = activeUpdateDialog.isChecked();

                    AlarmsManager.UpdateAlarm(getContext(), id, hours, minutes, name, active);

                    invalidDataText.setVisibility(View.INVISIBLE);

                    updateAlarmDialog.dismiss();

                    Toast.makeText(getContext(), "Alarm successfully changed", Toast.LENGTH_SHORT).show();

                    GetAlarms();
                    updateAlarmsList();

                } else {
                    invalidDataText.setVisibility(View.VISIBLE);
                }
            }
        });

        deleteUpdateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateAlarmDialog.dismiss();
                AlarmsManager.DeleteAlarm(getContext(), id);
                GetAlarms();
                updateAlarmsList();
            }
        });


        cancelUpdateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmMinutes.setValue(0);
                alarmHours.setValue(0);

                alarmName.setText(" ");

                invalidDataText.setVisibility(View.INVISIBLE);

                updateAlarmDialog.dismiss();
            }
        });

        updateAlarmDialog.show();
    }
}