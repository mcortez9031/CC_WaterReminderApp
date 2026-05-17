package com.example.waterreminder;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Daily_Goal extends AppCompatActivity {
    Button adjust, start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daily_goal);

        adjust = findViewById(R.id.btn_adjust);
        start = findViewById(R.id.btn_start);

        adjust.setOnClickListener(v -> {

        });

        start.setOnClickListener(v -> {

        });


        }
}