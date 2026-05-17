package com.example.waterreminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "waterReminder";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager =
                (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Android 8+ Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Water Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );

            manager.createNotificationChannel(channel);
        }

        // Create Notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("Drink Water!")
                        .setContentText("Time to drink water.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        // Show Notification
        manager.notify(1, builder.build());
    }
}