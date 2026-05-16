package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.material.slider.Slider;
import com.google.android.material.button.MaterialButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AgeSelection extends AppCompatActivity {

    private Slider ageSlider;
    private MaterialButton btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_age_selection);

        initViews();
        setupAgeSlider();
        setupButtons();
    }

    private void initViews() {
        ageSlider = findViewById(R.id.age_slider);
        btnContinue = findViewById(R.id.btn_next);
    }

    private void setupAgeSlider() {
        // Set initial age display
        updateAgeDisplay((int) ageSlider.getValue());

        // Slider value change listener
        ageSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                updateAgeDisplay((int) value);
            }
        });
    }

    private void updateAgeDisplay(int age) {
        // Find the TextView and update it
        if (findViewById(R.id.tv_age) != null) {
            ((android.widget.TextView) findViewById(R.id.tv_age)).setText(String.valueOf(age));
        }
    }

    private void setupButtons() {
        // Back Button
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());

        // Continue Button
        btnContinue.setOnClickListener(v -> {
            int selectedAge = (int) ageSlider.getValue();

            saveAgeToPreferences(selectedAge);

            Toast.makeText(AgeSelection.this,
                    "Age selected: " + selectedAge + " years old",
                    Toast.LENGTH_SHORT).show();

            // TODO: Navigate to next screen
            // startActivity(new Intent(AgeSelection.this, NextActivity.class));
            Intent intent = new Intent(AgeSelection.this, WeightSelection.class);
            startActivity(intent);
            // finish();
        });
    }

    private void saveAgeToPreferences(int age) {
        SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("user_age", age);
        editor.apply();
    }
}