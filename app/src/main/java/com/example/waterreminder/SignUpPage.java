package com.example.waterreminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpPage extends AppCompatActivity {

    // TextInputLayouts
    private TextInputLayout tilUsername, tilEmail, tilPassword, tilConfirmPassword;

    // EditTexts inside the layouts
    private TextInputEditText etUsernameInput, etEmailInput, etPasswordInput, etConfirmPasswordInput;

    Button btnSignUp;

    AlertDialog.Builder builder;
    AlertDialog.Builder successMessage;

    String gender, activity_level, weather;
    int weight;
    double water_goal;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(SignUpPage.this);

        // Initialize AlertDialog builders
        builder = new AlertDialog.Builder(this);
        successMessage = new AlertDialog.Builder(this);

        // ====================== Find Views ======================
        tilUsername = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);           // ← Was missing
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        btnSignUp = findViewById(R.id.btnSignUp);

        // Get the actual EditText from each TextInputLayout
        etUsernameInput = (TextInputEditText) tilUsername.getEditText();
        etEmailInput = (TextInputEditText) tilEmail.getEditText();
        etPasswordInput = (TextInputEditText) tilPassword.getEditText();
        etConfirmPasswordInput = (TextInputEditText) tilConfirmPassword.getEditText();

        // Safety check
        if (etUsernameInput == null || etEmailInput == null ||
                etPasswordInput == null || etConfirmPasswordInput == null) {
            displayMessage("Error", "Failed to initialize input fields");
            return;
        }

        btnSignUp.setOnClickListener(v -> {
            String username = etUsernameInput.getText().toString().trim();
            String email = etEmailInput.getText().toString().trim();
            String password = etPasswordInput.getText().toString().trim();
            String confirmPassword = etConfirmPasswordInput.getText().toString().trim();

            // Empty field check
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                displayMessage("Input Error!", "Please fill all fields");
                return;
            }

            // Password confirmation check
            if (!password.equals(confirmPassword)) {
                displayMessage("Password Mismatch", "Passwords do not match.");
                return;
            }

            // Password strength
            String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-+=])(?=\\S+$).{8,20}$";
            if (!password.matches(PASSWORD_PATTERN)) {
                displayMessage("Weak Password",
                        "Password must be 8-20 characters long with uppercase, lowercase, number, and special character.");
                return;
            }

            // Email validation
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                displayMessage("Invalid Email", "Please enter a valid email address.");
                return;
            }

            // Load user profile data
            SharedPreferences sharedPref = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
            gender = sharedPref.getString("gender", "Not Specified");
            weight = sharedPref.getInt("weight", 60);
            activity_level = sharedPref.getString("activity_level", "Moderate");
            weather = sharedPref.getString("weather", "mild");

            water_goal = weight * 35.0;

            if ("hot".equals(weather)) {
                water_goal += 500;
            } else if ("mild".equals(weather)) {
                water_goal += 250;
            }

            // Add user to database
            boolean success = databaseHelper.addUser(username, password, email,
                    gender, weight, activity_level, water_goal, weather);

            if (success) {
                AlertDialog.Builder successBuilder = new AlertDialog.Builder(this);

                successBuilder.setCancelable(false);
                successBuilder.setTitle("Success");
                successBuilder.setMessage("Welcome to AquaFill!");
                successBuilder.setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(SignUpPage.this, Daily_Goal.class);
                    intent.putExtra("water_goal", water_goal);
                    startActivity(intent);
                    finish();
                });

                successBuilder.show();

            } else {
                displayMessage("Registration Failed", "Username or email may already exist.");
            }
        });
    }

    public void displayMessage(String title, String message) {
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}