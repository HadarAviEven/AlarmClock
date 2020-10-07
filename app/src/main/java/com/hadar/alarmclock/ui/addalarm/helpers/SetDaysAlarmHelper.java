package com.hadar.alarmclock.ui.addalarm.helpers;

import android.content.Context;

import com.hadar.alarmclock.ui.addalarm.enums.Days;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;

import java.util.ArrayList;

public class SetDaysAlarmHelper {

    public static void setSelectedDaysAlarm(Context context, Alarm alarm) {
        SetAlarmHelper setAlarmHelper = new SetAlarmHelper(context);
        ArrayList<Days> originSelectedDays = alarm.getSelectedDays();

        for (int i = 0; i < originSelectedDays.size(); i++) {
            ArrayList<Days> nextDayArray = new ArrayList<>();
            nextDayArray.add(originSelectedDays.get(i));
            alarm.setSelectedDays(nextDayArray);
            setAlarmHelper.startAlarm(alarm, false);
        }
        alarm.setSelectedDays(originSelectedDays);
    }
}
