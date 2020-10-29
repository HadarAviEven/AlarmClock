package com.hadar.alarmclock.ui.main.events;

import com.hadar.alarmclock.ui.addalarm.enums.EventType;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;

public class AlarmEvent {
    private EventType eventType;
    private Alarm alarm;
    private boolean status;

    public AlarmEvent(EventType eventType, Alarm alarm) {
        this.eventType = eventType;
        this.alarm = alarm;
    }

    public AlarmEvent(EventType eventType, Alarm alarm, boolean status) {
        this.eventType = eventType;
        this.alarm = alarm;
        this.status = status;
    }

    public String getEventType() {
        return eventType.getType();
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public boolean getStatus() {
        return status;
    }
}
