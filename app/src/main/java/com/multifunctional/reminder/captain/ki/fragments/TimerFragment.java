package com.multifunctional.reminder.captain.ki.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.multifunctional.reminder.captain.ki.R;
import com.multifunctional.reminder.captain.ki.utils.TimerAlertService;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import one.xcorp.widget.swipepicker.SwipePicker;

public class TimerFragment extends Fragment {

    View view;

    private static final long START_TIME_IN_MILLIS = 600000;

    private TextView textViewCountDown;
    private ImageView buttonStartPause;
    private ImageView buttonReset;

    private CountDownTimer mCountDownTimer;

    private boolean timerRunning;

    private long timeLeftInMillis = 0;

    SwipePicker timerMinutesPicker, timerSecondsPicker;

    private int minutesPicked = 0, secondsPicked = 0;

    CircularProgressIndicator progressMinutes, progressSeconds;

    TextView hintText;

    String timeLeftFormatted = "";

    private static final CircularProgressIndicator.ProgressTextAdapter TIME_TEXT_ADAPTER = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double time) {
            return "";
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_timer, container, false);

        timerMinutesPicker = view.findViewById(R.id.timer_minutes_picker);
        timerSecondsPicker = view.findViewById(R.id.timer_seconds_picker);

        timerMinutesPicker.setHintTextColor(getResources().getColor(R.color.white));
        timerSecondsPicker.setHintTextColor(getResources().getColor(R.color.white));

        progressMinutes = view.findViewById(R.id.timer_progres_minutes);
        progressSeconds = view.findViewById(R.id.timer_progres_seconds);

        progressMinutes.setMaxProgress(60);
        progressSeconds.setMaxProgress(60);

        progressMinutes.setCurrentProgress(0);
        progressSeconds.setCurrentProgress(0);

        progressMinutes.setShouldDrawDot(false);
        progressSeconds.setShouldDrawDot(false);

        progressMinutes.setVisibility(View.INVISIBLE);
        progressSeconds.setVisibility(View.INVISIBLE);

        progressMinutes.setProgressTextAdapter(TIME_TEXT_ADAPTER);
        progressSeconds.setProgressTextAdapter(TIME_TEXT_ADAPTER);

        hintText = view.findViewById(R.id.timer_hint_text);

        timerMinutesPicker.setOnValueChangeListener(new SwipePicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(@NotNull SwipePicker swipePicker, float v, float v1) {
                minutesPicked = (int) v1;
                timeLeftInMillis = (minutesPicked * 1000 * 60) + (secondsPicked * 1000);
                updateCountDownText(timeLeftInMillis);
            }
        });

        timerSecondsPicker.setOnValueChangeListener(new SwipePicker.OnValueChangeListener() {
            @Override
            public void onValueChanged(@NotNull SwipePicker swipePicker, float v, float v1) {
                secondsPicked = (int) v1;
                timeLeftInMillis = (minutesPicked * 1000 * 60) + (secondsPicked * 1000);
                updateCountDownText(timeLeftInMillis);
            }
        });


        textViewCountDown = view.findViewById(R.id.text_view_countdown);

        buttonStartPause = view.findViewById(R.id.button_start_pause);
        buttonReset = view.findViewById(R.id.button_reset);

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    pauseTimer();
                } else {
                    if ((timerMinutesPicker.getValue() > 0) || (timerSecondsPicker.getValue() > 0)) {
                        startTimer();
                    } else {
                        Toast.makeText(getContext(), "Input time value first", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        updateCountDownText(timeLeftInMillis);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static TimerFragment newInstance() {
        TimerFragment fragment1 = new TimerFragment();
        Bundle args = new Bundle();
        fragment1.setArguments(args);
        return fragment1;
    }


    private void startTimer() {

        timerMinutesPicker.setEnabled(false);
        timerSecondsPicker.setEnabled(false);

        timerMinutesPicker.setVisibility(View.INVISIBLE);
        timerSecondsPicker.setVisibility(View.INVISIBLE);
        hintText.setVisibility(View.INVISIBLE);

        progressMinutes.setVisibility(View.VISIBLE);
        progressSeconds.setVisibility(View.VISIBLE);

        mCountDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText(timeLeftInMillis);
                updateProgress(timeLeftInMillis);
                timerMinutesPicker.setActivated(false);
                timerSecondsPicker.setActivated(false);
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                resetTimer();
                buttonStartPause.setImageResource(R.drawable.ic_start_timer_button);
                timerMinutesPicker.setEnabled(false);
                timerSecondsPicker.setEnabled(false);

                try {
                    getActivity().startService(new Intent(getActivity(), TimerAlertService.class));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();

        timerRunning = true;
        buttonStartPause.setImageResource(R.drawable.ic_pause_timer_button);
        buttonReset.setVisibility(View.GONE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        timerRunning = false;
        buttonStartPause.setImageResource(R.drawable.ic_start_timer_button);
        buttonReset.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        timeLeftInMillis = START_TIME_IN_MILLIS;
        textViewCountDown.setText("00 : 00");
        buttonReset.setVisibility(View.GONE);
        buttonStartPause.setVisibility(View.VISIBLE);
        timerMinutesPicker.setValue(0f);
        timerSecondsPicker.setValue(0f);

        progressMinutes.setVisibility(View.INVISIBLE);
        progressSeconds.setVisibility(View.INVISIBLE);

        timerMinutesPicker.setVisibility(View.VISIBLE);
        timerSecondsPicker.setVisibility(View.VISIBLE);
        hintText.setVisibility(View.VISIBLE);


    }

    private void updateCountDownText(long time) {
        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;

        timeLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

        textViewCountDown.setText(timeLeftFormatted);
    }

    private void updateProgress(long time) {
        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;

        progressMinutes.setCurrentProgress(minutes);
        progressSeconds.setCurrentProgress(seconds);
    }
}