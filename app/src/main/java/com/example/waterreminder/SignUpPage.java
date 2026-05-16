package com.example.waterreminder;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.util.Patterns;
import androidx.appcompat.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpPage extends AppCompatActivity {
EditText etUsername, etEmail, etPassword, etConfirmPassword;
Button btnSignUp;
AlertDialog.Builder builder, successMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);

        etUsername  =   findViewById(R.id.tilFullName);
        etEmail =   findViewById(R.id.tilEmail);
        etPassword  =   findViewById(R.id.etPassword);
        etConfirmPassword   = findViewById(R.id.etConfirmPassword);

        if (etUsername.getText().toString().trim().isEmpty() || etPassword.getText().toString().trim().isEmpty()
                || etEmail.getText().toString().trim().isEmpty()|| etConfirmPassword.getText().toString().trim().isEmpty()) {
            displayMessage("Input Error!", "Please fill all fields");
            return;
        }
        String PASSWORD_PATTERN =
                "^(?=.*[0-9])" +         // at least one digit
                        "(?=.*[a-z])" +         // at least one lowercase letter
                        "(?=.*[A-Z])" +         // at least one uppercase letter
                        "(?=.*[!@#$%^&*()-+=])" + // at least one special character
                        "(?=\\S+$)" +           // no whitespace allowed
                        ".{8,20}$";

        if (!etPassword.getText().toString().trim().matches(PASSWORD_PATTERN)) {
            displayMessage("Weak Password",
                    "Password must be at least 8 characters long, including " +
                            "uppercase, lowercase, a number, and a special character (@#$%^&+=!).");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            displayMessage("Invalid Email", "Please enter a valid email address.");
            return;
        }

        //add user to database
        boolean success = DatabaseHelper.addUser(etUsername, etPassword, etEmail, etGender, etWeight, activity_level, water_goal);

        if (success) {
            successMessage.setCancelable(false);
            successMessage.setTitle("Success");
            successMessage.setMessage("Welcome to HeronHealth!");
    }

    }

    public void displayMessage(String title, String message){
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}