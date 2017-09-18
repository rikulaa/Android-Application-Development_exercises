package com.example.riku.a6_customnotificationsdialogstoastexample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int notification_id = 0;
    private NotificationManager mNotifyMgr;
    private NotificationCompat.Builder mBuilder;
    private Timer timer;
    private TimerTask task;
    private boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isFirstTime) {
            showToast("Welcome to this awesome app!");
        }
    }

    @Override
    public void onPause() {
        // user leaves
        super.onPause();

        createNotification();
        timer = new Timer();
        task = new TimerTask() {
            int sec = 0;
            @Override
            public void run() {
                updateNotification(sec++);
            }
        };
        // 1 sec
        timer.schedule(task, 1000, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        // user has returned!
        cancelTimer();
        if (!isFirstTime) {
            showToast("Oh your back again, GREAT!");
        }
        isFirstTime = false;

    }

    // create notification
    public void createNotification() {
       mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.android)
                        .setContentTitle("Hey...!")
                        .setContentText("You have been gone for: 0 seconds. Please come back!");

        notification_id++;

        // Gets an instance of the NotificationManager service`
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(notification_id, mBuilder.build());
    }

    // update notification
    public void updateNotification(int sec) {
        mBuilder.setContentText("You have been gone for: " + sec + " seconds. Please come back!");
        mNotifyMgr.notify(notification_id, mBuilder.build());
    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void handleButtonClick(View view) {
        showToast("Oh, yeah I dont really do anything..");
    }

    public void cancelTimer() {
        // stop the task if previously set
        if (timer != null) {
            timer.cancel();
        }
    }


}
