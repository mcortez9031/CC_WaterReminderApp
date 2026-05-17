package com.example.waterreminder;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Daily_Goal extends AppCompatActivity {
    Button start;
    TextView water_goal;
    double waterGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_goal);

        start = findViewById(R.id.btn_start);
        water_goal = findViewById(R.id.tv_goal);
        start.setOnClickListener(v -> {

        });

        waterGoal = getIntent().getDoubleExtra("water_goal", 0.0);
        water_goal.setText(String.valueOf(waterGoal));

    }
}