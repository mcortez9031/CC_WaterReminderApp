package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WeatherSelection extends AppCompatActivity {
Button next;
CardView hot, cold, mild;
ImageView back;
    String tempt = "mild"; // Default fallback selection
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_selection);

        hot = findViewById(R.id.temptHot);
        cold = findViewById(R.id.temptCold);
        mild = findViewById(R.id.temptMild);
        back = findViewById(R.id.btn_back);
        next = findViewById(R.id.btn_next);

        next.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Temperature", tempt);
            editor.apply();
            Intent intent = new Intent(WeatherSelection.this, SignUpPage.class);
            startActivity(intent);
        });

        hot.setOnClickListener(v -> {
            hot.setCardBackgroundColor(Color.parseColor("#FFFFBA54"));
            cold.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            mild.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            tempt = "hot";
        });

        cold.setOnClickListener(v -> {
            cold.setCardBackgroundColor(Color.parseColor("#FF007EE3"));
            hot.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            mild.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            tempt = "cold";
        });
        mild.setOnClickListener(v -> {
            mild.setCardBackgroundColor(Color.parseColor("#FFC5E6EA"));
            cold.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            hot.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            tempt = "mild";
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(WeatherSelection.this, ExerciseSelection.class);
            startActivity(intent);

        });
    }
}