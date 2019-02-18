package com.example.lab6_1;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent tapIntent = new Intent(context, MainActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, tapIntent, 0);

        ReminderApplication application = ReminderApplication.getInstance();
        ReminderData reminderData = application.getReminderData();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, ReminderApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Your reminder!")
                .setContentText(reminderData.getText())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(234, mBuilder.build());
    }
}
