package com.multifunctional.reminder.captain.ki.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.multifunctional.reminder.captain.ki.R;

import java.util.Locale;

public class StopWatchFragment extends Fragment {

    View view;
    private TextView textViewStopWatch;
    private ImageView buttonStartPause;
    private ImageView buttonReset;

    Handler handler;

    ImageView minutesArrow;
    ImageView secondsArrow;

    int minutes = 0, seconds = 0;

    boolean stopWatchRunning = false;


    public static StopWatchFragment newInstance() {
        StopWatchFragment fragment1 = new StopWatchFragment();
        Bundle args = new Bundle();
        fragment1.setArguments(args);
        return fragment1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        textViewStopWatch = view.findViewById(R.id.text_view_countdown);

        buttonStartPause = view.findViewById(R.id.button_start_pause);
        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopWatchRunning) {
                    pauseStopWatch();
                } else {
                    startStopWatch();
                }
            }
        });

        minutesArrow = view.findViewById(R.id.stopwatch_arrow_minutes);
        secondsArrow= view.findViewById(R.id.stopwatch_arrow_seconds);


        buttonReset = view.findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStopWatch();
            }
        });

        handler = new Handler();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void startStopWatch() {

        buttonStartPause.setImageResource(R.drawable.ic_pause_stopwatch_button);
        buttonReset.setVisibility(View.GONE);

        handler.postDelayed(runnable, 500);
        stopWatchRunning = true;
    }

    private void pauseStopWatch() {
        handler.removeCallbacks(runnable);

        buttonStartPause.setImageResource(R.drawable.ic_start_stopwatch_button);
        buttonReset.setVisibility(View.VISIBLE);

        stopWatchRunning = false;
    }

    private void resetStopWatch() {
        stopWatchRunning = false;
        buttonStartPause.setImageResource(R.drawable.ic_start_stopwatch_button);
        buttonReset.setVisibility(View.GONE);
        seconds = 0;
        minutes = 0;

        secondsArrow.setRotation(0);
        minutesArrow.setRotation(0);
        textViewStopWatch.setText("00 : 00");

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (minutes < 60) {
                if (seconds < 59) {
                    seconds++;

                } else {
                    seconds = 0;
                    minutes++;
                }
                secondsArrow.setRotation((float) (seconds*6));
                minutesArrow.setRotation((float) (minutes*6));

                textViewStopWatch.setText(String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds));
                handler.postDelayed(this, 1000);
            } else {
                pauseStopWatch();
            }
        }
    };
}