package com.hadar.alarmclock.ui.main;

import android.view.View;
import android.widget.TextView;

import com.hadar.alarmclock.R;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    protected SwitchCompat status;
    protected TextView time;
    protected TextView sunday;
    protected TextView monday;
    protected TextView tuesday;
    protected TextView wednesday;
    protected TextView thursday;
    protected TextView friday;
    protected TextView saturday;

    AlarmViewHolder(@NonNull View itemView) {
        super(itemView);

        findViews();
    }

    private void findViews() {
        status = itemView.findViewById(R.id.status);
        time = itemView.findViewById(R.id.timeTextView);
        sunday = itemView.findViewById(R.id.sundayTextView);
        monday = itemView.findViewById(R.id.mondayTextView);
        tuesday = itemView.findViewById(R.id.tuesdayTextView);
        wednesday = itemView.findViewById(R.id.wednesdayTextView);
        thursday = itemView.findViewById(R.id.thursdayTextView);
        friday = itemView.findViewById(R.id.fridayTextView);
        saturday = itemView.findViewById(R.id.saturdayTextView);
    }
}