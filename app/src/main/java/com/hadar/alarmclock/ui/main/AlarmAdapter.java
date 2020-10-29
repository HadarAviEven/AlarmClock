package com.hadar.alarmclock.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hadar.alarmclock.data.db.AlarmDatabase;
import com.hadar.alarmclock.data.db.AppExecutors;
import com.hadar.alarmclock.ui.addalarm.enums.EventType;
import com.hadar.alarmclock.ui.addalarm.helpers.SetAlarmHelper;
import com.hadar.alarmclock.R;
import com.hadar.alarmclock.ui.addalarm.enums.Days;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;
import com.hadar.alarmclock.ui.main.events.AlarmEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class AlarmAdapter extends EmptyRecyclerView.Adapter<AlarmViewHolder> {

    private ArrayList<Alarm> alarmsArrayList;
    private Context context;
    private AlarmDatabase mDb;

    public AlarmAdapter(Context context) {
        this.context = context;
        mDb = AlarmDatabase.getInstance(context);
    }

    public void setData(ArrayList<Alarm> newAlarmsArrayList) {
        this.alarmsArrayList = newAlarmsArrayList;
        notifyDataSetChanged();
    }

    public void addAlarm(Alarm alarm) {
        alarmsArrayList.add(alarm);
        notifyDataSetChanged();
    }

    public void removeAlarm(Alarm alarm) {
        alarmsArrayList.remove(alarm);
        notifyDataSetChanged();
    }

    public void updateAlarm(Alarm alarm, boolean isChecked) {
        alarm.setStatus(isChecked);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item, parent, false);
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

    private void setStatus(final AlarmViewHolder holder, final Alarm currentItem, final int position) {
//        holder.status.setChecked(currentItem.getStatus());
//
//        holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                changeAlarmStatus(position, isChecked);
//                changeAlarmVisibility(holder.time, isChecked);
//                changeAlarmSetHelper(position, false);
//            }
//        });


//        holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                changeAlarmStatus(position, isChecked);
//                changeAlarmVisibility(holder.time, isChecked);
//                changeAlarmSetHelper(position, false);
//            }
//        });
//
//        holder.status.setChecked(currentItem.getStatus());


        holder.status.setOnCheckedChangeListener(null);

        holder.status.setChecked(currentItem.getStatus());

        holder.status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateStatus(position, isChecked);
                updateTimeColor(holder.time, isChecked);
                updateDaysColor(holder, currentItem);
            }
        });


//        holder.status.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isChecked = holder.status.isChecked();
//                updateStatus(position, isChecked);
//                updateTimeColor(holder.time, isChecked);
//                updateDaysColor(holder, currentItem);
//                updateAlarmSetHelper(position, false);
//            }
//        });
//
//        holder.status.setChecked(currentItem.getStatus());
    }

    private void updateStatus(final int position, final boolean isChecked) {
        updateAlarmSetHelper(position, false);
        updateData(position, isChecked);
    }

    private void updateAlarmSetHelper(int position, boolean isDeleted) {
        SetAlarmHelper setAlarmHelper = new SetAlarmHelper(context);
        setAlarmHelper.setAlarm(alarmsArrayList.get(position), isDeleted);
    }

    private void setTime(AlarmViewHolder holder, Alarm currentItem) {
        String time = currentItem.getStringHour() + ":" + (currentItem.getStringMinute());
        holder.time.setText(time);
        updateTimeColor(holder.time, currentItem.getStatus());
    }

    private void updateTimeColor(TextView time, boolean isChecked) {
        if (!isChecked) {
            updateTextViewColor(time, ContextCompat.getColor(context, R.color.gray));
        } else {
            updateTextViewColor(time, ContextCompat.getColor(context, R.color.black));
        }
    }

    private void setDays(AlarmViewHolder holder, Alarm currentItem) {
        setTextDay(holder);
        updateDaysColor(holder, currentItem);
    }

    private void setTextDay(AlarmViewHolder holder) {
//        for (Days day: Days.values()) {
//        }
        holder.sunday.setText(R.string.sunday);
        holder.monday.setText(R.string.monday);
        holder.tuesday.setText(R.string.tuesday);
        holder.wednesday.setText(R.string.wednesday);
        holder.thursday.setText(R.string.thursday);
        holder.friday.setText(R.string.friday);
        holder.saturday.setText(R.string.saturday);
    }

    private void updateDaysColor(AlarmViewHolder holder, Alarm currentItem) {
//        for (Days day: Days.values()) {
//        }
        updateSpecificDayColor(currentItem, holder.sunday, Days.Sun);
        updateSpecificDayColor(currentItem, holder.monday, Days.Mon);
        updateSpecificDayColor(currentItem, holder.tuesday, Days.Tue);
        updateSpecificDayColor(currentItem, holder.wednesday, Days.Wed);
        updateSpecificDayColor(currentItem, holder.thursday, Days.Thu);
        updateSpecificDayColor(currentItem, holder.friday, Days.Fri);
        updateSpecificDayColor(currentItem, holder.saturday, Days.Sat);
    }

    private void updateSpecificDayColor(Alarm currentItem, TextView holderDay, Days day) {
        if (currentItem.getSelectedDays().contains(Days.valueOf(day.name()))) {
            if (!currentItem.getStatus()) {
                updateTextViewColor(holderDay, ContextCompat.getColor(context, R.color.light_blue));
            } else {
                updateTextViewColor(holderDay, ContextCompat.getColor(context, R.color.blue));
            }
        } else {
            updateTextViewColor(holderDay, ContextCompat.getColor(context, R.color.gray));
        }
    }

    private void updateTextViewColor(TextView textView, int color) {
        textView.setTextColor(color);
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
                updateAlarmSetHelper(position, true);
                deleteData(position);
            }
        });

        alertDialogBuilder.setNegativeButton("No", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        if (alarmsArrayList == null)
            return 0;
        return alarmsArrayList.size();
    }

    public void updateData(int position, boolean isChecked) {
        final Alarm alarmToUpdate = alarmsArrayList.get(position);
        EventBus.getDefault().post(new AlarmEvent(new EventType(EventType.Type.UPDATE), alarmToUpdate, isChecked));

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.e(" alarmToUpdate", "id " + alarmToUpdate.getId() + " status " + alarmToUpdate.getStatus());
                mDb.alarmDao().updateAlarm(alarmToUpdate);

                List<Alarm> list = mDb.alarmDao().loadAllAlarms();
                EventBus.getDefault().post(list);
            }
        });
    }

    public void deleteData(int position) {
        final Alarm alarmToRemove = alarmsArrayList.get(position);
        Log.e(" alarmToRemove", "id " + alarmToRemove.getId());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.alarmDao().deleteAlarm(alarmToRemove);

                List<Alarm> list = mDb.alarmDao().loadAllAlarms();
                EventBus.getDefault().post(list);
            }
        });
        EventBus.getDefault().post(new AlarmEvent(new EventType(EventType.Type.DELETE), alarmToRemove));
    }
}
