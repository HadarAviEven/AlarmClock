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
                    addToAlarmManager(calendar);
//                    Log.e("SetAlarmHelper", "Alarm is set for: " + calendar.getTime() + "id: " + alarm.getId());
                } else { // alarm has no days selected
                    if (calendar.before(Calendar.getInstance())) { // time passed for today
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    addToAlarmManager(calendar);
//                    Log.e("SetAlarmHelper", "Alarm set for: " + calendar.getTime() + "id: " + alarm.getId());
                }
            } else { // alarm is off
                cancelFromAlarmManager();
//                Log.w("SetAlarmHelper", "Alarm canceled, id: " + alarm.getId());
            }
        } else { // alarm is deleted
            cancelFromAlarmManager();
//            Log.w("SetAlarmHelper", "Alarm deleted, id: " + alarm.getId());
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

    private void addToAlarmManager(Calendar calendar) {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelFromAlarmManager() {
        alarmManager.cancel(pendingIntent);
    }
}
