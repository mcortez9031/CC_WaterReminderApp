package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
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
    private ImageButton btnBack;
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
        ageSlider = findViewById(R.id.ageSlider);
        btnContinue = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupAgeSlider() {
        updateAgeDisplay((int) ageSlider.getValue());

        ageSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                updateAgeDisplay((int) value);
            }
        });
    }

    private void updateAgeDisplay(int age) {
        if (findViewById(R.id.tvAge) != null) {
            ((android.widget.TextView) findViewById(R.id.tvAge)).setText(String.valueOf(age));
        }
    }

    private void setupButtons() {
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());

        btnContinue.setOnClickListener(v -> {
            int selectedAge = (int) ageSlider.getValue();

            saveAgeToPreferences(selectedAge);

            Toast.makeText(AgeSelection.this,
                    "Age selected: " + selectedAge + " years old",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AgeSelection.this, WeightSelection.class);
            startActivity(intent);
        });
    }

    private void saveAgeToPreferences(int age) {
        SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("user_age", age);
        editor.apply();
    }
}