package com.example.waterreminder;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    private TextView tvWaterIntake, tvDailyGoal;
    private ProgressBar progressBar;
    private EditText etAmount;
    private Button btnAdd, btnReminder, btnHisto, btnAcc;
    private DatabaseHelper databaseHelper;
    private String email = "";
    private int dailyGoal = 0;
    private int selectedHour   = 20;
    private int selectedMinute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        try {
            tvWaterIntake = findViewById(R.id.tvWaterIntake);
            progressBar = findViewById(R.id.progressBar);
            etAmount = findViewById(R.id.etAmount);
            btnAdd = findViewById(R.id.btnAdd);
            btnReminder = findViewById(R.id.btnReminder);
            btnHisto = findViewById(R.id.btnHisto);
            tvDailyGoal = findViewById(R.id.tvDailyGoal);
            btnAcc = findViewById(R.id.btnAcc);


            databaseHelper = new DatabaseHelper(this);

            SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
            email = sharedPref.getString("email", "");

            if (email.isEmpty()) {
                Toast.makeText(this, "Please login again", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, UserLogIn.class));
                finish();
                return;
            }

            dailyGoal = databaseHelper.waterGoal(email);

            createNotificationChannel();
            updateUI();

            btnAdd.setOnClickListener(v -> addWater());
            btnAcc.setOnClickListener(view -> {
                startActivity(new Intent(Dashboard.this, AccountView.class));
            });

            btnReminder.setOnClickListener(v -> {

                android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                        Toast.makeText(this, "Please enable exact alarms permission for reminders.", Toast.LENGTH_LONG).show();
                        android.content.Intent settingsIntent = new android.content.Intent(
                                android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                                android.net.Uri.parse("package:" + getPackageName()));
                        startActivity(settingsIntent);
                        return; // Stop execution until permission is granted
                    }
                }

                TimePickerDialog dialog = new TimePickerDialog(
                        this,
                        android.R.style.Theme_DeviceDefault_Dialog_Alert,
                        (view, hourOfDay, minute) -> {
                            selectedHour = hourOfDay;
                            selectedMinute = minute;

                            java.util.Calendar calendar = java.util.Calendar.getInstance();
                            calendar.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(java.util.Calendar.MINUTE, minute);
                            calendar.set(java.util.Calendar.SECOND, 0);

                            if (calendar.before(java.util.Calendar.getInstance())) {
                                calendar.add(java.util.Calendar.DATE, 1);
                            }

                            android.content.Intent intent = new android.content.Intent(this, ReminderReceiver.class);

                            int flags = android.app.PendingIntent.FLAG_UPDATE_CURRENT;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                flags |= android.app.PendingIntent.FLAG_IMMUTABLE;
                            }

                            android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(
                                    this,
                                    0,
                                    intent,
                                    flags
                            );

                            if (alarmManager != null) {
                                try {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                        alarmManager.setExactAndAllowWhileIdle(
                                                android.app.AlarmManager.RTC_WAKEUP,
                                                calendar.getTimeInMillis(),
                                                pendingIntent
                                        );
                                    } else {
                                        alarmManager.setExact(
                                                android.app.AlarmManager.RTC_WAKEUP,
                                                calendar.getTimeInMillis(),
                                                pendingIntent
                                        );
                                    }

                                    String timeFormatted = String.format(java.util.Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                                    Toast.makeText(this, "Reminder scheduled for " + timeFormatted, Toast.LENGTH_SHORT).show();
                                } catch (SecurityException se) {
                                    Toast.makeText(this, "Security Error: Exact alarm permission missing.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        selectedHour,
                        selectedMinute,
                        false
                );
                dialog.setTitle("SELECT TIME");
                dialog.show();

            });


            btnHisto.setOnClickListener(v -> {
                startActivity(new Intent(Dashboard.this, History.class));
            });
        }catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            }
    }

    private void addWater() {
        if (etAmount == null) return;
        String input = etAmount.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int amount = Integer.parseInt(input);
            if (amount > 0) {
                databaseHelper.addLog(amount, email);
                etAmount.setText("");
                updateUI();
                Toast.makeText(this, "Water Added!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        if (tvWaterIntake == null || progressBar == null) return;

        int total = databaseHelper.getDailyTotal(email);
        tvWaterIntake.setText(String.valueOf(total));
        progressBar.setMax(dailyGoal);
        progressBar.setProgress(Math.min(total, dailyGoal));
        tvDailyGoal.setText(String.valueOf(dailyGoal));
    }

    private void setReminder() {

    }

    private void createNotificationChannel() {

    }
}