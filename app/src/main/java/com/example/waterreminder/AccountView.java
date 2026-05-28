package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.waterreminder.models.UserInfo;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AccountView extends AppCompatActivity {

    private MaterialButton btnHisto, btnDashboard, btnAcc, btnLogout;
    private ImageButton btnWeatherEdit, btnAgeEdit, btnWeightEdit, btnActLvlEdit;
    private TextView tvFullName, tvEmail, tvGender, tvAge, tvActivityLevel, tvWeather, tvWeight;
    private DatabaseHelper databaseHelper;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_view);

        btnAcc = findViewById(R.id.btnAcc);
        btnHisto = findViewById(R.id.btnHisto);
        btnDashboard = findViewById(R.id.btnDashboard);
        btnLogout = findViewById(R.id.btnLogout);
        btnWeatherEdit = findViewById(R.id.btnWeatherEdit);
        btnAgeEdit = findViewById(R.id.btnAgeEdit);
        btnWeightEdit = findViewById(R.id.btnWeightEdit);
        btnActLvlEdit = findViewById(R.id.btnActLvlEdit);
        tvFullName = findViewById(R.id.tvFullNameValue);
        tvAge = findViewById(R.id.tvAgeValue);
        tvWeight = findViewById(R.id.tvWeightValue);
        tvEmail = findViewById(R.id.tvEmailValue);
        tvGender = findViewById(R.id.tvGenderValue);
        tvActivityLevel = findViewById(R.id.tvActivityLevelValue);
        tvWeather = findViewById(R.id.tvWeatherValue);
        databaseHelper = new DatabaseHelper(this);
        SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
        email = sharedPref.getString("email", "");
        loadData(email);
        setOnCLickListeners();
    }

    public void setOnCLickListeners(){
        btnWeatherEdit.setOnClickListener(v -> showEditDialog("Edit Weather", tvWeather, false, "weather"));
        btnAgeEdit.setOnClickListener(v -> showEditDialog("Edit Your Age", tvAge, true, "age"));
        btnWeightEdit.setOnClickListener(v -> showEditDialog("Edit Your Weight", tvWeight, true, "weight"));
        btnActLvlEdit.setOnClickListener(v -> showEditDialog("Edit Your Activity Level", tvActivityLevel, false, "activity"));
        btnDashboard.setOnClickListener(view -> {startActivity(new Intent(AccountView.this, Dashboard.class));});
        btnLogout.setOnClickListener(view -> {logoutUser();});
        btnHisto.setOnClickListener(view -> {startActivity(new Intent(AccountView.this, History.class));});
    }

    private void showEditDialog(String title, TextView target, boolean numericInput, String fieldType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final EditText input = new EditText(this);
        input.setText(target.getText().toString());

        if (numericInput) {
            input.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER
                            | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );
        }
        builder.setView(input);

        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String val = input.getText().toString().trim();

            if (validateInput(val, input, fieldType)) {
                target.setText(val);
                saveFieldToDatabase(fieldType);
                dialog.dismiss();
            }
        });
    }

    private boolean validateInput(String val, EditText input, String fieldType) {
        if (val.isEmpty()) {
            input.setError("This field cannot be empty");
            return false;
        }

        switch (fieldType) {
            case "age":
                try {
                    int age = Integer.parseInt(val);
                    if (age < 1 || age > 120) {
                        input.setError("Please enter a valid age between 1 and 120");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    input.setError("Please enter a whole number");
                    return false;
                }
                break;

            case "weight":
                try {
                    double weight = Double.parseDouble(val);
                    if (weight < 20.0 || weight > 300.0) {
                        input.setError("Please enter a realistic weight between 20kg and 300kg");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    input.setError("Please enter a valid weight number");
                    return false;
                }
                break;

            case "weather":
                String weather= val.toLowerCase();
                if (!weather.equals("hot") && !weather.equals("mild") && !weather.equals("cold")) {
                    input.setError("Acceptable options: hot, mild, cold");
                    return false;
                }
                break;

            case "activity":
                String act = val.toLowerCase();
                if (!act.equals("active") && !act.equals("moderate") &&
                        !act.equals("light") && !act.equals("rarely")) {
                    input.setError("Options: rarely, light, moderate, active");
                    return false;
                }
                break;
        }
        return true;
    }

    private void saveFieldToDatabase(String fieldType) {
        String currentGender = tvGender.getText().toString().trim();
        String currentWeather = tvWeather.getText().toString().trim();
        String currentActivity = tvActivityLevel.getText().toString().trim();
        String ageStr = tvAge.getText().toString().trim();
        String weightStr = tvWeight.getText().toString().trim();

        int currentAge = 0;
        int currentWeight = 0;
        try {
            if (!ageStr.isEmpty()) currentAge = Integer.parseInt(ageStr);
            if (!weightStr.isEmpty()) currentWeight = (int) Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        boolean isSaveSuccessful = false;
        String successMessage = "";

        switch (fieldType) {
            case "weather":
                isSaveSuccessful = databaseHelper.updateUserWeather(currentWeather, email);
                successMessage = "New Weather saved!";
                break;

            case "age":
                isSaveSuccessful = databaseHelper.updateUserAge(currentAge, email);
                successMessage = "Age updated successfully!";
                break;

            case "weight":
                isSaveSuccessful = databaseHelper.updateUserWeight(currentWeight, email);
                successMessage = "Weight updated successfully!";
                break;

            case "activity":
                isSaveSuccessful = databaseHelper.updateUserActivityLevel(currentActivity, email);
                successMessage = "Activity level updated!";
                break;
        }

        if (isSaveSuccessful) {
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();

            double newWaterGoal = updateCalculation(currentAge, currentWeather, currentWeight, currentActivity, currentGender);
            databaseHelper.updateWaterGoal(newWaterGoal, email);
        } else {
            Toast.makeText(this, "Error saving updates to the database.", Toast.LENGTH_SHORT).show();
        }
    }

    public double updateCalculation(int age, String weather, int weight, String activityLevel, String gender){
        double water_goal = weight * 35.0;

        if ("hot".equalsIgnoreCase(weather)) {
            water_goal += 500;
        } else if ("mild".equalsIgnoreCase(weather)) {
            water_goal += 250;
        }


        if (age > 55){
            water_goal *= 0.90;
        } else if (age < 18) {
            water_goal *= 1.10;
        }


        if(weight <= 0){
            water_goal = 2000;
        }


        if ("Male".equalsIgnoreCase(gender) || "M".equalsIgnoreCase(gender)) {
            water_goal *= 1.10;

            switch (activityLevel.toLowerCase()) {
                case "active":
                    water_goal *= 1.30;
                    break;
                case "moderate":
                    water_goal *= 1.15;
                    break;
                case "light":
                case "rarely":
                    water_goal *= 0.95;
                    break;
            }
        }
        return water_goal;
    }

    private void logoutUser() {
        SharedPreferences sharedPref = getSharedPreferences("user_profile", android.content.Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(AccountView.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void loadData(String email){
        ArrayList<UserInfo> userInformation = databaseHelper.getUserInfo(email);
        if (userInformation != null && !userInformation.isEmpty()) {
            UserInfo info = userInformation.get(0);
            tvFullName.setText(info.getName());
            tvWeight.setText(String.valueOf(info.getWeight()));
            tvGender.setText(info.getGender());
            tvAge.setText(String.valueOf(info.getAge()));
            tvWeather.setText(info.getWeather());
            tvActivityLevel.setText(info.getActivityLevel());
            tvEmail.setText(info.getEmail());
        }
    }
}