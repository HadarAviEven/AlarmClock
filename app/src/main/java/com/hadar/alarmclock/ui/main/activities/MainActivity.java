package com.hadar.alarmclock.ui.main.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hadar.alarmclock.data.AlarmsDataManager;
import com.hadar.alarmclock.R;
import com.hadar.alarmclock.ui.addalarm.activities.AddAlarmActivity;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;
import com.hadar.alarmclock.ui.main.events.DataSyncEvent;
import com.hadar.alarmclock.ui.main.AlarmAdapter;

import org.greenrobot.eventbus.Subscribe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    private FloatingActionButton fab;
    private final int LAUNCH_SECOND_ACTIVITY = 1;
    public static int UNIQUE_ID = -1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initRecyclerView();
        initFab();
    }

    private void findViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatingActionButton);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        alarmAdapter = new AlarmAdapter(AlarmsDataManager.getInstance().alarmsArrayList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(alarmAdapter);
    }

    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAlarm();
            }
        });
    }

    private void addNewAlarm() {
        Intent intent = new Intent(this, AddAlarmActivity.class);
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                Alarm resultAlarm = data.getParcelableExtra(getString(R.string.result_alarm));
                AlarmsDataManager.getInstance().addAlarm(resultAlarm);
                alarmAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(DataSyncEvent syncStatusMessage) {
        alarmAdapter.notifyDataSetChanged();
    }
}