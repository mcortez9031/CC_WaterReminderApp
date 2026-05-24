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

            btnReminder.setOnClickListener(v -> {
                TimePickerDialog dialog = new TimePickerDialog(
                        this,
                        android.R.style.Theme_DeviceDefault_Dialog_Alert,
                        (view, hourOfDay, minute) -> {
                            selectedHour   = hourOfDay;
                            selectedMinute = minute;

                        },
                        selectedHour,
                        selectedMinute,
                        false
                );
                dialog.setTitle("SELECT TIME");
                dialog.show();
            });

            btnHisto.setOnClickListener(v ->
                    Toast.makeText(this, "History coming soon...", Toast.LENGTH_SHORT).show());

        } catch (Exception e) {
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
        tvWaterIntake.setText(total);
        progressBar.setMax(dailyGoal);
        progressBar.setProgress(Math.min(total, dailyGoal));
        tvDailyGoal.setText(dailyGoal);
    }

    private void setReminder() {

    }

    private void createNotificationChannel() {

    }
}