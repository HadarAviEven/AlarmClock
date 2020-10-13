package com.hadar.alarmclock.ui.main.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hadar.alarmclock.R;

import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), context.getString(R.string.Boot))) {

            Intent newIntent = new Intent(context, AlarmReceiver.class);
            context.sendBroadcast(newIntent);

//            String msg = "Alarm after restart!!!";
//            Log.e("AlarmReceiver", msg);
        }
    }
}
