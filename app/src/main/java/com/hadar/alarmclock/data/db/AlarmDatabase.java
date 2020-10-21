package com.hadar.alarmclock.data.db;

import android.content.Context;
import android.util.Log;

import com.hadar.alarmclock.data.db.converters.DaysConverter;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = Alarm.class, exportSchema = false, version = 1)
@TypeConverters({DaysConverter.class})
public abstract class AlarmDatabase extends RoomDatabase {
    private static final String LOG_TAG = AlarmDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "alarm";
    private static AlarmDatabase sInstance;

    public static AlarmDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AlarmDatabase.class, AlarmDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract AlarmDao alarmDao();
}