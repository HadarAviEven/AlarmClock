package com.hadar.alarmclock.ui.addalarm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.hadar.alarmclock.data.db.converters.DaysConverter;
import com.hadar.alarmclock.ui.addalarm.helpers.TimeConverterHelper;
import com.hadar.alarmclock.ui.addalarm.enums.Days;

import java.util.ArrayList;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "alarm")
public class Alarm implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "status")
    private boolean status;
    @ColumnInfo(name = "hour")
    private int hour;
    @ColumnInfo(name = "minute")
    private int minute;
    @TypeConverters(DaysConverter.class)
    public ArrayList<Days> days;

    public Alarm() {

    }

    public Alarm(boolean newStatus, int newHour, int newMinute, ArrayList<Days> newDays) {
        status = newStatus;
        hour = newHour;
        minute = newMinute;
        days = newDays;
    }

    public boolean getStatus() {
        return status;
    }

    public int getHour() {
        return hour;
    }

    public int getIntHour() {
        return hour;
    }

    public String getStringHour() {
        return TimeConverterHelper.getStringPresentation(hour);
    }

    public int getMinute() {
        return minute;
    }

    public int getIntMinute() {
        return minute;
    }

    public String getStringMinute() {
        return TimeConverterHelper.getStringPresentation(minute);
    }

    public ArrayList<Days> getSelectedDays() {
        return days;
    }

    public int getId() {
        return id;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSelectedDays(ArrayList<Days> days) {
        this.days = days;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeInt(this.hour);
        dest.writeInt(this.minute);
        dest.writeList(this.days);
        dest.writeInt(this.id);
    }

    protected Alarm(Parcel in) {
        this.status = in.readByte() != 0;
        this.hour = in.readInt();
        this.minute = in.readInt();
        this.days = new ArrayList<>();
        in.readList(this.days, Days.class.getClassLoader());
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Alarm> CREATOR = new Parcelable.Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel source) {
            return new Alarm(source);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };
}
