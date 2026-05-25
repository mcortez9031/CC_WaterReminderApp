package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE);
        boolean logIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (logIn){
            startActivity(new Intent(MainActivity.this, Dashboard.class));
        }
        setContentView(R.layout.activity_main);

        next=findViewById(R.id.btn_next);
        next.setOnClickListener(v ->{
            Intent intent= new Intent(MainActivity.this, UserLogIn.class);
            startActivity(intent);
        });

    }
}