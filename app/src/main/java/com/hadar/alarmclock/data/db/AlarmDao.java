package com.hadar.alarmclock.data.db;

import com.hadar.alarmclock.ui.addalarm.models.Alarm;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AlarmDao {

    @Query("SELECT * FROM alarm ORDER BY ID")
    List<Alarm> loadAllAlarms();

    @Insert
    long insertAlarm(Alarm alarm);

    @Update
    void updateAlarm(Alarm alarm);

    @Delete
    void deleteAlarm(Alarm alarm);

    @Query("SELECT * FROM alarm WHERE id = :id")
    Alarm loadAlarmById(int id);
}
