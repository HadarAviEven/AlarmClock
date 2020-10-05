package com.hadar.alarmclock.ui.addalarm.helpers;

public class TimeConverterHelper {

    private static final int MAX_SINGLE_DIGIT = 9;

    public static String getStringPresentation(int time) {
        String stringTime = String.valueOf(time);
        if (time <= MAX_SINGLE_DIGIT) {
            stringTime = "0" + stringTime;
        }
        return stringTime;
    }
}
