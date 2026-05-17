package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExerciseSelection extends AppCompatActivity {
    Button next;
    ImageView back;
    CardView rarely, light, moderate, active;
    String activity_level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exercise_selection);

        rarely = findViewById(R.id.optRarely);
        light = findViewById(R.id.optLight);
        moderate=findViewById(R.id.optModerate);
        active = findViewById(R.id.optActive);
        back = findViewById(R.id.btnBack);
        next = findViewById(R.id.btnNext);

        next.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Activity Level", activity_level);
            editor.apply();
            Intent intent = new Intent(ExerciseSelection.this, WeatherSelection.class);
            startActivity(intent);
        });

        rarely.setOnClickListener(v -> {
            rarely.setCardBackgroundColor(Color.parseColor("#FF479FF6"));
            activity_level = "rarely";
        });

        light.setOnClickListener(v -> {
            light.setCardBackgroundColor(Color.parseColor("#FF479FF6"));
            activity_level = "light";
        });
        moderate.setOnClickListener(v -> {
            moderate.setCardBackgroundColor(Color.parseColor("#FF479FF6"));
            activity_level = "moderate";
        });

        active.setOnClickListener(v -> {
            active.setCardBackgroundColor(Color.parseColor("#FF479FF6"));
            activity_level = "active";
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(ExerciseSelection.this, WeightSelection.class);
            startActivity(intent);
        });
    }
    public void unselectItem(String activity_level){
        if(activity_level == "rarely"){
            rarely.setCardBackgroundColor(Color.parseColor("#F0F9FF"));
        }
        if(activity_level == "light"){
            light.setCardBackgroundColor(Color.parseColor("#F0F9FF"));
        }
        if(activity_level == "moderate"){
            moderate.setCardBackgroundColor(Color.parseColor("#F0F9FF"));
        }
        if(activity_level == "active"){
            active.setCardBackgroundColor(Color.parseColor("#F0F9FF"));
        }
    }
}