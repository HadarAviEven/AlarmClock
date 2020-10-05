package com.hadar.alarmclock.ui.addalarm.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hadar.alarmclock.R;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;
import com.hadar.alarmclock.ui.main.receivers.AlarmReceiver;

import java.util.Calendar;

public class SetAlarmHelper {

    private Context context;

    public SetAlarmHelper(Context context) {
        this.context = context;
    }

    public void startAlarm(Alarm alarm, boolean isDeleted) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.context, AlarmReceiver.class);
        intent.putExtra(context.getString(R.string.id), alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context,
                intent.getIntExtra(context.getString(R.string.id), -1), intent, 0);

        if (!isDeleted) {
            if (alarm.getStatus()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarm.getIntHour());
                calendar.set(Calendar.MINUTE, alarm.getIntMinute());
                calendar.set(Calendar.SECOND, 0);

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.cancel(pendingIntent);
            }
        } else {
            alarmManager.cancel(pendingIntent);
        }
    }
}
