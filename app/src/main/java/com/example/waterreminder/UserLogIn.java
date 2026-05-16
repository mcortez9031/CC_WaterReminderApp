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

public class UserLogIn extends AppCompatActivity {
    Button signIn;
    EditText email, password;
    TextView signUp;

    AlertDialog.Builder builder, successMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_log_in);

        signIn=findViewById(R.id.btn_login);
        email=findViewById(R.id.til_email);
        password=findViewById(R.id.til_password);
        signUp=findViewById(R.id.tv_sign_up);

        signIn.setOnClickListener(v ->{
            DatabaseHelper databaseHelper = new DatabaseHelper(UserLogIn.this);
            boolean userFound = databaseHelper.searchUser(email.getText().toString().trim(), password.getText().toString().trim());
            if (!userFound){
                displayMessage("LogIn Error!", "Incorrect username or password");
            }else {
                SharedPreferences sharedPref = getSharedPreferences("HeronHealthPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putBoolean("isLoggedIn", true);
                editor.putString("userEmail", email.getText().toString().trim());
                editor.apply();
                Intent intent = new Intent(UserLogIn.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(v ->{
            Intent intent = new Intent(UserLogIn.this, SignUpPage.class);
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