package com.example.studysupportapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Log.e("Event from Reminder", extras.getString(FragmentAddSchedule.notificationKey));
        if(extras!=null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notificationSchedule")
                    .setSmallIcon(R.drawable.search_icon)
                    .setContentTitle(extras.getString(FragmentAddSchedule.notificationKey))
                    .setContentText(extras.getString(FragmentAddSchedule.notificationContentKey))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(Integer.parseInt(extras.getString(FragmentAddSchedule.notificationTagKey)),builder.build());
        }
    }
}
