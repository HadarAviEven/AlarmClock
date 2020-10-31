package com.hadar.alarmclock.ui.main.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.hadar.alarmclock.ui.addalarm.activities.AddAlarmActivity;
import com.hadar.alarmclock.ui.realtimealarm.activities.RealTimeAlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
//        mediaPlayer.start();

        String msg = "Alarm!!!";
        Log.e("AlarmReceiver", msg);

//        Intent intent1 = new Intent(context, RealTimeAlarmActivity.class);
//        startActivity(intent1);

    }
}
