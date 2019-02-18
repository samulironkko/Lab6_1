package com.example.lab6_1;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ReminderApplication extends Application {

    static final String CHANNEL_ID = "NOTIFICATION_CHANNEL_ID";
    static ReminderApplication instance = null;

    ReminderData reminderData = new ReminderData();

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        instance = this;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder";
            String description = "Reminder description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static ReminderApplication getInstance() { return instance; }

    public ReminderData getReminderData() {return reminderData; }
}
