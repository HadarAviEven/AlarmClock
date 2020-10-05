package com.hadar.alarmclock.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hadar.alarmclock.ui.addalarm.helpers.SetAlarmHelper;
import com.hadar.alarmclock.data.AlarmsDataManager;
import com.hadar.alarmclock.R;
import com.hadar.alarmclock.ui.addalarm.enums.Days;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private ArrayList<Alarm> alarmsArrayList;
    private Context context;

    public AlarmAdapter(ArrayList<Alarm> newAlarmsArrayList, Context context) {
        this.alarmsArrayList = newAlarmsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmViewHolder holder, final int position) {

        Alarm currentItem = alarmsArrayList.get(position);
        setAlarm(holder, position, currentItem);
        setListener(holder, position);
    }

    private void setAlarm(final AlarmViewHolder holder, final int position, Alarm currentItem) {
        setStatus(holder, currentItem, position);
        setTime(holder, currentItem);
        setDays(holder, currentItem);
    }

    private void setStatus(final AlarmViewHolder holder, Alarm currentItem, final int position) {
        holder.status.setChecked(currentItem.getStatus());

        holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeAlarmStatus(position, isChecked);
                changeAlarmVisibility(holder.time, isChecked);
                changeAlarmSetHelper(position, false);
            }
        });
    }

    private void changeAlarmStatus(int position, boolean isChecked) {
        AlarmsDataManager.getInstance().setStatus(position, isChecked);
    }

    private void changeAlarmVisibility(TextView time, boolean isChecked) {
        if (!isChecked) {
            turnOnOffAlarm(time, ContextCompat.getColor(context, R.color.gray));
        } else {
            turnOnOffAlarm(time, ContextCompat.getColor(context, R.color.black));
        }
    }

    private void changeAlarmSetHelper(int position, boolean isDeleted) {
        SetAlarmHelper setAlarmHelper = new SetAlarmHelper(context);
        setAlarmHelper.startAlarm(alarmsArrayList.get(position), isDeleted);
    }

    private void setTime(AlarmViewHolder holder, Alarm currentItem) {
        String time = currentItem.getStringHour() + ":" + (currentItem.getStringMinute());
        holder.time.setText(time);
    }

    private void setDays(AlarmViewHolder holder, Alarm currentItem) {
//        for (Days day: Days.values()) {
//        }
        setDaysColor(currentItem, holder.sunday, Days.Sun);
        setDaysColor(currentItem, holder.monday, Days.Mon);
        setDaysColor(currentItem, holder.tuesday, Days.Tue);
        setDaysColor(currentItem, holder.wednesday, Days.Wed);
        setDaysColor(currentItem, holder.thursday, Days.Thu);
        setDaysColor(currentItem, holder.friday, Days.Fri);
        setDaysColor(currentItem, holder.saturday, Days.Sat);
    }

    private void setDaysColor(Alarm currentItem, TextView holderDay, Days day) {
        if (currentItem.getSelectedDays().contains(Days.valueOf(day.name()))) {
            holderDay.setTextColor(ContextCompat.getColor(context, R.color.blue));
        } else {
            holderDay.setTextColor(ContextCompat.getColor(context, R.color.gray));
        }
    }

    private void turnOnOffAlarm(TextView time, int color) {
        time.setTextColor(color);
    }

    private void setListener(final AlarmViewHolder holder, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startDeleteAlarmDialog(position);
                return true;
            }
        });
    }

    private void startDeleteAlarmDialog(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.delete_alarm_dialog);
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                changeAlarmSetHelper(position, true);
                AlarmsDataManager.getInstance().removeAlarm(position);
            }
        });
        alertDialogBuilder.setNegativeButton("No", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return alarmsArrayList.size();
    }
}
