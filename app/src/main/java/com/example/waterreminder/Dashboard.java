package com.example.waterreminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    TextView tvWaterIntake;
    ProgressBar progressBar;
    EditText etAmount;
    Button btnAdd, btnReminder;
    DatabaseHelper databaseHelper;
    int dailyGoal;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        tvWaterIntake = findViewById(R.id.tvWaterIntake);
        progressBar = findViewById(R.id.progressBar);
        etAmount = findViewById(R.id.etAmount);
        btnAdd = findViewById(R.id.btnAdd);
        btnReminder = findViewById(R.id.btnReminder);
        databaseHelper = new DatabaseHelper(this);
        SharedPreferences sharedPref = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        email = sharedPref.getString("email", "");
        dailyGoal = databaseHelper.waterGoal(email);

        createNotificationChannel();
        updateUI();

        btnAdd.setOnClickListener(v -> {

            String input = etAmount.getText().toString().trim();

            if (!input.isEmpty()) {

                int amount = Integer.parseInt(input);

                databaseHelper.addLog(amount, email);

                etAmount.setText("");

                updateUI();

                Toast.makeText(
                        Dashboard.this,
                        "Water Added!",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        Dashboard.this,
                        "Please enter water amount",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // Reminder Button
        btnReminder.setOnClickListener(v -> {

            setReminder();

            Toast.makeText(
                    Dashboard.this,
                    "Reminder Set Every 2 Hours",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private void updateUI() {

        int total = databaseHelper.getDailyTotal(email);

        tvWaterIntake.setText(total + " / " + dailyGoal + " ml");

        progressBar.setMax(dailyGoal);

        progressBar.setProgress(Math.min(total, dailyGoal));
    }

    // AlarmManager Reminder
    private void setReminder() {

        Intent intent = new Intent(this, ReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE |
                        PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        long interval =  60 * 2 ;

        long triggerTime = System.currentTimeMillis() + interval;

        if (alarmManager != null) {

            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    interval,
                    pendingIntent
            );
        }
    }

    // Notification Channel
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    ReminderReceiver.CHANNEL_ID,
                    "Water Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Water Intake Reminder Notifications");

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {

                manager.createNotificationChannel(channel);
            }
        }
    }
    private void setHourlyReminder() {

        Intent intent = new Intent(this, ReminderReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(ALARM_SERVICE);

        // 1 hour in milliseconds
        long interval = 60 * 60 * 1000;

        long triggerTime = System.currentTimeMillis() + interval;

        if (alarmManager != null) {

            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    interval,
                    pendingIntent
            );
        }
    }
}

