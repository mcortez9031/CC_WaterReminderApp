package com.example.waterreminder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterreminder.models.WaterLogInfo;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    private TextView tvTodayIntake;
    private RecyclerView recyclerHistory;
    private LinearLayout emptyStateLayout;
    private DatabaseHelper databaseHelper;
    private String email = "";
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);   // Make sure this matches your layout filename

        // Initialize Views
        tvTodayIntake = findViewById(R.id.tvTodayIntake);
        recyclerHistory = findViewById(R.id.recyclerHistory);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);

        databaseHelper = new DatabaseHelper(this);

        // Get logged in user email
        SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
        email = sharedPref.getString("email", "");

        // Setup RecyclerView
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));

        // Load Data
        loadTodayIntake();
        loadHistory();
    }

    private void loadTodayIntake() {
        if (email.isEmpty()) return;

        int todayTotal = databaseHelper.getDailyTotal(email);
        tvTodayIntake.setText(todayTotal + " ml");
    }

    private void loadHistory() {
        if (email.isEmpty()) {
            showEmptyState(true);
            return;
        }

        ArrayList<WaterLogInfo> historyList = databaseHelper.getHistory(email);

        if (historyList.isEmpty()) {
            showEmptyState(true);
        } else {
            showEmptyState(false);

            historyAdapter = new HistoryAdapter(historyList);
            recyclerHistory.setAdapter(historyAdapter);
        }
    }

    private void showEmptyState(boolean show) {
        emptyStateLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        recyclerHistory.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this screen
        loadTodayIntake();
        loadHistory();
    }
}