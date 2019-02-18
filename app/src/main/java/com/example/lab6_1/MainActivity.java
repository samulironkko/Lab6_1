package com.example.lab6_1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String SHARED_PREF_FILE = "PrefFile";
    static final String SHARED_PREF_TEXT_KEY = "textInTheEditor";
    static final String SHARED_PREF_HOUR_KEY = "hoursInThePicker";
    static final String SHARED_PREF_MINUTE_KEY = "minutesInThePicker";

    TimePicker timePicker;
    Button doneButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);

        editText = findViewById(R.id.edit_text);

    }

    @Override
    public void onClick(View v) {
        ReminderApplication application = ReminderApplication.getInstance();
        ReminderData reminderData = application.getReminderData();

        reminderData.setText(editText.getText().toString());
        reminderData.setHours(timePicker.getCurrentHour());
        reminderData.setMinutes(timePicker.getCurrentMinute());

        SharedPreferences sharedPreferences = getPref();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SHARED_PREF_TEXT_KEY, reminderData.getText());
        editor.commit();

        editor.putInt(SHARED_PREF_HOUR_KEY, reminderData.getHours());
        editor.commit();

        editor.putInt(SHARED_PREF_MINUTE_KEY, reminderData.getMinutes());
        editor.commit();

        int time = reminderData.getHours() * 60 * 60 + reminderData.getMinutes() * 60;
        startBroadcast(time);

        finish();
    }


    protected SharedPreferences getPref() {
        return getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReminderApplication application = ReminderApplication.getInstance();
        ReminderData reminderData = application.getReminderData();
        SharedPreferences sharedPreferences = getPref();

        String text = sharedPreferences.getString(SHARED_PREF_TEXT_KEY, null);
        int hours = sharedPreferences.getInt(SHARED_PREF_HOUR_KEY, 0);
        int minutes = sharedPreferences.getInt(SHARED_PREF_MINUTE_KEY, 0);

        reminderData.setText(text);
        reminderData.setHours(hours);
        reminderData.setMinutes(minutes);

        editText.setText(text);
        timePicker.setCurrentHour(hours);
        timePicker.setCurrentMinute(minutes);
    }

    protected void startBroadcast(int time) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderBroadcastReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        time * 1000, alarmIntent);

    }
}
