package com.hadar.alarmclock.ui.main.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hadar.alarmclock.R;
import com.hadar.alarmclock.data.db.AlarmDatabase;
import com.hadar.alarmclock.data.db.AppExecutors;
import com.hadar.alarmclock.ui.addalarm.activities.AddAlarmActivity;
import com.hadar.alarmclock.ui.addalarm.enums.EventType;
import com.hadar.alarmclock.ui.addalarm.helpers.SetAlarmHelper;
import com.hadar.alarmclock.ui.addalarm.models.Alarm;
import com.hadar.alarmclock.ui.main.EmptyRecyclerView;
import com.hadar.alarmclock.ui.main.AlarmAdapter;
import com.hadar.alarmclock.ui.main.events.AlarmEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

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
                insertData(data);
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
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(List<Alarm> list) {
        Log.e("onEvent", "show");
        for (Alarm alarm : list) {
            Log.e("MainActivity", "alarm id = " + alarm.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AlarmEvent alarmEvent) {
        String type = alarmEvent.getEventType();

        switch (type) {
            case "insert":
//                Log.e("onEvent", "inserted to adapter");
                alarmAdapter.addAlarm(alarmEvent.getAlarm());
                break;
            case "delete":
//                Log.e("onEvent", "deleted from adapter");
                alarmAdapter.removeAlarm(alarmEvent.getAlarm());
                break;
            case "update":
//                Log.e("onEvent", "updated in adapter");
                alarmAdapter.updateAlarm(alarmEvent.getAlarm(), alarmEvent.getStatus());
                break;
            default:
                break;
        }
    }

    public void insertData(Intent data) {
        final Alarm resultAlarm = data.getParcelableExtra(getString(R.string.result_alarm));
        final long[] newId = new long[1];

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // insert to Database
                newId[0] = mDb.alarmDao().insertAlarm(resultAlarm);
//                Log.e("MainActivity", "run: newId = " + newId[0]);
//                Log.e("MainActivity", "inserted to Database");

//                List<Alarm> list = mDb.alarmDao().loadAllAlarms();
//                EventBus.getDefault().post(list);

                // insert to Adapter
                resultAlarm.setId((int) newId[0]);
                EventBus.getDefault().post(new AlarmEvent(new EventType(EventType.Type.INSERT), resultAlarm));

                // insert to Alarms queue
                updateSetAlarmHelper(getApplicationContext(), resultAlarm, false);
//                Log.e("MainActivity", "inserted to alarms queue");
            }
        });
    }

    public static void updateSetAlarmHelper(Context context, Alarm alarm, boolean isDeleted) {
        SetAlarmHelper setAlarmHelper = new SetAlarmHelper(context);
        setAlarmHelper.setAlarm(alarm, isDeleted);
    }
}