package com.hadar.alarmclock.data.db.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hadar.alarmclock.ui.addalarm.enums.Days;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.room.TypeConverter;

public class DaysConverter {
    @TypeConverter
    public static ArrayList<Days> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Days>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Days> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
