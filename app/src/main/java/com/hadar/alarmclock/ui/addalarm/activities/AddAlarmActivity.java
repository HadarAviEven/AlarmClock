package com.hadar.alarmclock.ui.addalarm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.hadar.alarmclock.ui.addalarm.helpers.SetAlarmHelper;
import com.hadar.alarmclock.ui.addalarm.enums.Days;
import com.hadar.alarmclock.R;
import com.hadar.alarmclock.ui.addalarm.helpers.TimeConverterHelper;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;

import java.util.ArrayList;

public class AddAlarmActivity extends AppCompatActivity {
    private Button setAlarmBtn;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private ArrayList<String> hourPickerValues;
    private ArrayList<String> minutePickerValues;
    private int currentHourValue;
    private int currentMinuteValue;
    private ArrayList<Days> selectedDays;
    private final int HOUR_ARRAY_SIZE = 24;
    private final int MINUTE_ARRAY_SIZE = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        this.setTitle(getString(R.string.add_new_alarm));

        findViews();
        initLists();
        initAllPickers();
        setBtn();
    }

    public void onClickDayButton(View v) {
        Button day = (Button) v;
        Days convertedDay = Days.valueOf(day.getText().toString());

        if (!selectedDays.contains(convertedDay)) {
            day.setTextColor(ContextCompat.getColor(this, R.color.blue));
            selectedDays.add(convertedDay);
        } else {
            day.setTextColor(ContextCompat.getColor(this, R.color.black));
            selectedDays.remove(convertedDay);
        }
    }

    private void findViews() {
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        setAlarmBtn = findViewById(R.id.setAlarmBtn);
    }

    private void initLists() {
        selectedDays = new ArrayList<>();
        hourPickerValues = new ArrayList<>();
        minutePickerValues = new ArrayList<>();
    }

    private void setBtn() {
        setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int selectedHour = getSelectedHour();
                int selectedMinute = getSelectedMinute();

                Alarm alarm = new Alarm(true, selectedHour, selectedMinute, selectedDays);

                returnIntent(alarm);
                setAlarmHelper(alarm);

                showRemainingTimeToast();
            }
        });
    }

    private int getSelectedHour() {
        return Integer.parseInt(hourPickerValues.get(currentHourValue));
    }

    private int getSelectedMinute() {
        return Integer.parseInt(minutePickerValues.get(currentMinuteValue));
    }

    private void returnIntent(Alarm alarm) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(getString(R.string.result_alarm), alarm);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void setAlarmHelper(Alarm alarm) {
        SetAlarmHelper setAlarmHelper = new SetAlarmHelper(getApplicationContext());
        setAlarmHelper.startAlarm(alarm, false);
    }

    private void initAllPickers() {

        String[] tempHourValues = setPickerValues(HOUR_ARRAY_SIZE, hourPickerValues);
        setPicker(hourPicker, tempHourValues);

        String[] tempMinuteValues = setPickerValues(MINUTE_ARRAY_SIZE, minutePickerValues);
        setPicker(minutePicker, tempMinuteValues);
    }

    private String[] setPickerValues(int arraySize, ArrayList<String> pickerValues) {
        for (int i = 0; i < arraySize; i++) {
            pickerValues.add(TimeConverterHelper.getStringPresentation(i));
        }
        return pickerValues.toArray(new String[arraySize]);
    }

    private void setPicker(NumberPicker picker, String[] pickerValues) {
        picker.setMinValue(0);
        picker.setMaxValue(pickerValues.length - 1);

        picker.setDisplayedValues(pickerValues);

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int i, int i1) {
                if (picker.equals(hourPicker)) {
                    currentHourValue = picker.getValue();
                } else {
                    currentMinuteValue = picker.getValue();
                }
            }
        });
    }

    private void showRemainingTimeToast() {
        Toast.makeText(this, "Remaining Time Toast", Toast.LENGTH_SHORT).show();
    }
}



