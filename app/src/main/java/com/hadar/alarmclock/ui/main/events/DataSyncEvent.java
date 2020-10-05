package com.hadar.alarmclock.ui.main.events;

public class DataSyncEvent {
    private final String syncStatusMessage;

    public DataSyncEvent(String syncStatusMessage) {
        this.syncStatusMessage = syncStatusMessage;
    }

    public String getSyncStatusMessage() {
        return syncStatusMessage;
    }
}
