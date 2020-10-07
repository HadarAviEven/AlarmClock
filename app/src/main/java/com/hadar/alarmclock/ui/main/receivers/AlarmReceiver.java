package com.hadar.alarmclock.ui.main.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
//        mediaPlayer.start();

        Log.e("AlarmReceiver", "Alarm!!!");
    }
}
