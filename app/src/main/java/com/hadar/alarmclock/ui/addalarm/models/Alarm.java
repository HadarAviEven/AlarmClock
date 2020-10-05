package com.hadar.alarmclock.ui.addalarm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.hadar.alarmclock.ui.addalarm.helpers.TimeConverterHelper;
import com.hadar.alarmclock.ui.addalarm.enums.Days;
import com.hadar.alarmclock.ui.main.activities.MainActivity;

import java.util.ArrayList;

public class Alarm implements Parcelable {

    private boolean status;
    private int hour;
    private int minute;
    private ArrayList<Days> days;
    private int id;

    public Alarm(boolean newStatus, int newHour, int newMinute, ArrayList<Days> newDays) {
        status = newStatus;
        hour = newHour;
        minute = newMinute;
        days = newDays;
        id = ++MainActivity.UNIQUE_ID;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getIntHour() {
        return hour;
    }

    public String getStringHour() {
        return TimeConverterHelper.getStringPresentation(hour);
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

    public void clearSelectedDays() {
        days.clear();
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
        this.days = new ArrayList<Days>();
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
