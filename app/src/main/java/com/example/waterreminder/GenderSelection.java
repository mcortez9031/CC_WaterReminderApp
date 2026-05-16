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

public class GenderSelection extends AppCompatActivity {
CardView male, female;
Button next;
ImageView back;
String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gender_selection);

        male = findViewById(R.id.card_male);
        female = findViewById(R.id.card_female);
        next = findViewById(R.id.btn_next);
        back = findViewById(R.id.btn_back);

        next.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("gender", gender);
            editor.apply();
            Intent intent = new Intent(GenderSelection.this, AgeSelection.class);
            startActivity(intent);
        });

        male.setOnClickListener(v -> {
            male.setCardBackgroundColor(Color.parseColor("#FF479FF6"));
            gender = "male";
        });

        female.setOnClickListener(v -> {
            female.setCardBackgroundColor(Color.parseColor("FFF17CF1"));
            gender = "female";
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(GenderSelection.this, UserLogIn.class);
            startActivity(intent);
        });
    }
}