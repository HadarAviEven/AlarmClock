package com.hadar.alarmclock.ui.main.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hadar.alarmclock.R;

import java.util.Objects;

public class SampleBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), context.getString(R.string.Boot))) {
            Log.e("AlarmReceiver", "Alarm after restart!!!");
        }
    }
}
