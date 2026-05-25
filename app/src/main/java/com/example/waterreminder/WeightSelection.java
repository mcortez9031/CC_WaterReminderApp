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

public class WeightSelection extends AppCompatActivity {
    private Slider weightSlider;
    private MaterialButton btnContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weight_selection);

            initViews();
            setupWeightSlider();
            setupButtons();
        }

        private void initViews() {
            weightSlider = findViewById(R.id.weight_slider);
            btnContinue = findViewById(R.id.btn_next);
        }

        private void setupWeightSlider() {
            updateWeightDisplay((int) weightSlider.getValue());

            weightSlider.addOnChangeListener(new Slider.OnChangeListener() {
                @Override
                public void onValueChange(Slider slider, float value, boolean fromUser) {
                    updateWeightDisplay((int) value);
                }
            });
        }

        private void updateWeightDisplay(int weight) {
            if (findViewById(R.id.tv_weight) != null) {
                ((android.widget.TextView) findViewById(R.id.tv_weight)).setText(String.valueOf(weight));
            }
        }

        private void setupButtons() {
            findViewById(R.id.btn_back).setOnClickListener(v -> {
                Intent intent = new Intent(WeightSelection.this, AgeSelection.class);
                startActivity(intent);
            });

            btnContinue.setOnClickListener(v -> {
                int selectedWeight = (int) weightSlider.getValue();

                saveWeightToPreferences(selectedWeight);

                Toast.makeText(WeightSelection.this,
                        "Weight selected: " + selectedWeight + " kg",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(WeightSelection.this, ExerciseSelection.class);
                startActivity(intent);
            });
        }

        private void saveWeightToPreferences(int weight) {
            SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("weight", weight);
            editor.apply();
        }
    }