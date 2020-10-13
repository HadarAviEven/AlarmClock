package com.hadar.alarmclock.ui.addalarm.helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hadar.alarmclock.R;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;
import com.hadar.alarmclock.ui.main.receivers.AlarmReceiver;
import com.hadar.alarmclock.ui.main.receivers.BootReceiver;

import java.util.Calendar;

public class SetAlarmHelper {

    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public SetAlarmHelper(Context context) {
        this.context = context;
    }

    public void startAlarm(Alarm alarm, boolean isDeleted) {
        init(alarm);
        checkCases(alarm, isDeleted);
    }

    private void init(Alarm alarm) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.context, AlarmReceiver.class);
        intent.putExtra(context.getString(R.string.id), alarm.getId());
        pendingIntent = PendingIntent.getBroadcast(this.context,
                intent.getIntExtra(context.getString(R.string.id), -1), intent, 0);
    }

    private void checkCases(Alarm alarm, boolean isDeleted) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getIntHour());
        calendar.set(Calendar.MINUTE, alarm.getIntMinute());
        calendar.set(Calendar.SECOND, 0);

        if (!isDeleted) { // alarm is active
            if (alarm.getStatus()) { // alarm is on
                if (alarm.getSelectedDays().size() >= 2) { // alarm has several days selected
                    SetDaysAlarmHelper.setSelectedDaysAlarm(context, alarm);
                } else if (alarm.getSelectedDays().size() == 1) { // alarm has one day selected
                    setDay(calendar, alarm);
                    addAlarm(true, calendar);
                    Log.e("SetAlarmHelper", "repeat at " + calendar.getTime().toString());
                } else { // alarm has no days selected
                    addAlarm(false, calendar);
                    Log.e("SetAlarmHelper", "once at " + calendar.getTime().toString());
                }
            } else { // alarm is off
                cancelAlarm();
                Log.e("SetAlarmHelper", "canceled ");
            }
        } else { // alarm is deleted
            cancelAlarm();
            Log.e("SetAlarmHelper", "deleted ");
        }
    }

    private void setDay(Calendar calendar, Alarm alarm) {
        int intDayOfWeek = 0;
        switch (alarm.getSelectedDays().get(0)) {
            case Sun:
                intDayOfWeek = 1;
                break;
            case Mon:
                intDayOfWeek = 2;
                break;
            case Tue:
                intDayOfWeek = 3;
                break;
            case Wed:
                intDayOfWeek = 4;
                break;
            case Thu:
                intDayOfWeek = 5;
                break;
            case Fri:
                intDayOfWeek = 6;
                break;
            case Sat:
                intDayOfWeek = 7;
                break;
            default:
                break;
        }
        calendar.set(Calendar.DAY_OF_WEEK, intDayOfWeek);
    }

    private void ifAlreadyPassed(Calendar calendar, int numberOfDaysToAdd) {
        if (calendar.before(Calendar.getInstance())) { // time passed for today or for this week
            calendar.add(Calendar.DAY_OF_MONTH, numberOfDaysToAdd);
        }
    }

    private void addAlarm(boolean isRepeatedly, Calendar calendar) {
        if (isRepeatedly) {
            ifAlreadyPassed(calendar, 7);
            addToAlarmManagerRepeatedly(calendar);
        } else {
            ifAlreadyPassed(calendar, 1);
            addToAlarmManager(calendar);
        }
        addToBootReceiver();
    }

    private void cancelAlarm() {
        cancelFromAlarmManager();
        cancelFromBootReceiver();
    }

    private void addToAlarmManager(Calendar calendar) {
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), pendingIntent);
    }

    private void addToAlarmManagerRepeatedly(Calendar calendar) {
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY  /*1000 * 60 * 60 * 24*/ * 7, pendingIntent);

        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                /*AlarmManager.INTERVAL_DAY*/  1000 * 60 * 60 * 24 * 7, pendingIntent);
    }

    private void cancelFromAlarmManager() {
        alarmManager.cancel(pendingIntent);
    }

    private void addToBootReceiver() {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void cancelFromBootReceiver() {
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
