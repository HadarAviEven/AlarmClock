package com.hadar.alarmclock.data;

import com.hadar.alarmclock.ui.addalarm.models.Alarm;
import com.hadar.alarmclock.ui.main.events.DataSyncEvent;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class AlarmsDataManager {

    private static AlarmsDataManager instance = null;
    public ArrayList<Alarm> alarmsArrayList;

    private AlarmsDataManager() {
        alarmsArrayList = new ArrayList<>();
    }

    public static AlarmsDataManager getInstance() {
        if (instance == null) {
            instance = new AlarmsDataManager();
        }
        return instance;
    }

    public void addAlarm(Alarm alarm) {
        alarmsArrayList.add(alarm);
        notifyOnListChanged();
    }

    public void setStatus(int position, boolean isChecked) {
        alarmsArrayList.get(position).setStatus(isChecked);
    }

    public void removeAlarm(int position) {
        alarmsArrayList.get(position).clearSelectedDays();
        alarmsArrayList.remove(position);
        notifyOnListChanged();
    }

    public void removeAllAlarms() {
        alarmsArrayList.clear();
        notifyOnListChanged();
    }

    private void notifyOnListChanged() {
        EventBus.getDefault().post(new DataSyncEvent("Sync SuccessFully"));
    }
}