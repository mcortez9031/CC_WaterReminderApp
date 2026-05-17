package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class UserLogIn extends AppCompatActivity {
    Button signIn;
    EditText etEmailInput, etPasswordInput;   // Actual EditTexts
    TextView signUp;

    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_log_in);

        signIn = findViewById(R.id.btnLogin);
        signUp = findViewById(R.id.tvSignUp);
        TextInputLayout emailLayout = findViewById(R.id.etEmail);
        TextInputLayout passwordLayout = findViewById(R.id.etPassword);
        etEmailInput = emailLayout.getEditText();
        etPasswordInput = passwordLayout.getEditText();
        builder = new AlertDialog.Builder(this);

        signIn.setOnClickListener(v ->{
            DatabaseHelper databaseHelper = new DatabaseHelper(UserLogIn.this);
            boolean userFound = databaseHelper.searchUser(etEmailInput.getText().toString().trim(), etPasswordInput.getText().toString().trim());
            if (!userFound){
                displayMessage("LogIn Error!", "Incorrect username or password");
            }else {
                SharedPreferences sharedPref = getSharedPreferences("AquaFill", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putBoolean("isLoggedIn", true);
                editor.putString("userEmail", etEmailInput.getText().toString().trim());
                editor.apply();
                Intent intent = new Intent(UserLogIn.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(v ->{
            Intent intent = new Intent(UserLogIn.this, GenderSelection.class);
            startActivity(intent);
        });
    }

    public void displayMessage(String title, String message){
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}