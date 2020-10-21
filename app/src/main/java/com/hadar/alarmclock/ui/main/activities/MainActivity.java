package com.hadar.alarmclock.ui.main.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hadar.alarmclock.R;
import com.hadar.alarmclock.data.db.AlarmDatabase;
import com.hadar.alarmclock.data.db.AppExecutors;
import com.hadar.alarmclock.ui.addalarm.activities.AddAlarmActivity;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;
import com.hadar.alarmclock.ui.main.EmptyRecyclerView;
import com.hadar.alarmclock.ui.main.events.DataSyncEvent;
import com.hadar.alarmclock.ui.main.AlarmAdapter;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private EmptyRecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    private FloatingActionButton fab;
    private final int LAUNCH_SECOND_ACTIVITY = 1;
    private AlarmDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initRecyclerView();
        initFab();
        initDB();
        retrieveTasks();
    }

    private void initDB() {
        mDb = AlarmDatabase.getInstance(getApplicationContext());
    }

    private void findViews() {
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floatingActionButton);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        alarmAdapter = new AlarmAdapter(this);
        recyclerView.setAdapter(alarmAdapter);
        recyclerView.setEmptyView(findViewById(R.id.emptyView));
        recyclerView.initEmptyView();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                final Alarm resultAlarm = data.getParcelableExtra(getString(R.string.result_alarm));

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.alarmDao().insertAlarm(resultAlarm);

                        List<Alarm> list = mDb.alarmDao().loadAllAlarms();
                        for (Alarm alarm : list) {
                            Log.e("MainActivity", "alarm id = " + alarm.getId());
                        }
                    }
                });

//                ArrayList<Alarm> a = new ArrayList<>(list);
//                alarmAdapter.setData(a);

                alarmAdapter.addData(resultAlarm);//
            }
        }
    }

    private void retrieveTasks() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Alarm> alarmsList = mDb.alarmDao().loadAllAlarms();
                final ArrayList<Alarm> alarmsArrayList = new ArrayList<>(alarmsList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alarmAdapter.setData(alarmsArrayList);
                        recyclerView.initEmptyView();
                    }
                });
            }
        });
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
