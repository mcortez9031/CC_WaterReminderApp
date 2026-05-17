package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GenderSelection extends AppCompatActivity {
CardView male, female;
Button next;
ImageView back;
String gender = "";
AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gender_selection);

        male = findViewById(R.id.card_male);
        female = findViewById(R.id.card_female);
        next = findViewById(R.id.btn_next);
        back = findViewById(R.id.btn_back);
        builder = new AlertDialog.Builder(this);

        male.setOnClickListener(v -> selectGender(male, female, "male"));
        female.setOnClickListener(v -> selectGender(female, male, "female"));
        next.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("gender", gender);
            editor.apply();
            if (gender.isEmpty()){
                displayMessage("Missing", "Please select a Gender.");
                return;
            }
            Intent intent = new Intent(GenderSelection.this, AgeSelection.class);
            startActivity(intent);
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(GenderSelection.this, UserLogIn.class);
            startActivity(intent);
        });
    }

    private void selectGender(CardView selected, CardView other, String selectedGender) {
        selected.setCardBackgroundColor(Color.parseColor("#FF479FF6"));
        other.setCardBackgroundColor(Color.parseColor("#F0F9FF"));
        gender = selectedGender;
    }

    public void displayMessage(String title, String message){
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}