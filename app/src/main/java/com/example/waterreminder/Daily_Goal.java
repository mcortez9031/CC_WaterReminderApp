package com.example.waterreminder;

import android.content.Intent;
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
    TextView goal;
    double waterGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_goal);

        start = findViewById(R.id.btn_start);
        goal = findViewById(R.id.tv_goal);
        start.setOnClickListener(v -> {
            Intent intent = new Intent(Daily_Goal.this, Dashboard.class);
            startActivity(intent);

        });

        waterGoal = getIntent().getDoubleExtra("water_goal", 0.0);
        goal.setText(String.valueOf(waterGoal));
        goal.setText(waterGoal + " ml");
    }
}