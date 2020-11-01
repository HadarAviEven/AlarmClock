package com.hadar.alarmclock.ui.realtimealarm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

import com.hadar.alarmclock.R;

public class RealTimeAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_alarm);

//        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
//        mediaPlayer.start();
    }
}