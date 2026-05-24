package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterreminder.models.WaterLogInfo;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    private RecyclerView rvHistory;
    private DatabaseHelper databaseHelper;
    private Button btnDashboard, btnHisto, btnAcc;
    private MaterialButton btnBack, btnDelete, btnClearAll;
    private HistoryAdapter historyAdapter;
    private ArrayList<WaterLogInfo> waterLogInfos;
    private int selectedPosition = -1;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // 1. Fetch user email session right away
        SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
        email = sharedPref.getString("email", "");

        // 2. Initialize layout elements
        btnDashboard = findViewById(R.id.btnDashboard);
        btnHisto = findViewById(R.id.btnHisto);
        btnAcc = findViewById(R.id.btnAcc);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btn_delete);
        btnClearAll = findViewById(R.id.btn_clear_all);
        rvHistory = findViewById(R.id.rv_history);

        databaseHelper = new DatabaseHelper(this);
        waterLogInfos = new ArrayList<>();

        historyAdapter = new HistoryAdapter(waterLogInfos, (water, position) -> {
            selectedPosition = position;

            btnDelete.setEnabled(true);
            Toast.makeText(History.this, "Selected: " + water.getWaterIntake(), Toast.LENGTH_SHORT).show();
        });

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(historyAdapter);

        // Initially disable delete button until an item is clicked
        btnDelete.setEnabled(false);

        // 4. Setup Click Listeners
        setupNavigationListeners();
        setupActionListeners();

        // 5. Populate list from SQLite Database
        loadHistoryData();
    }

    private void loadHistoryData() {
        ArrayList<WaterLogInfo> freshLogs = databaseHelper.getAllLogs(email);
        if (freshLogs == null) {
            freshLogs = new ArrayList<>();
        }

        // Use the adapter's built-in update method to keep animations fluid
        historyAdapter.updateData(freshLogs);

        // Reset selected position tracking when data reloads
        selectedPosition = -1;
        btnDelete.setEnabled(false);
    }

    private void setupActionListeners() {
        // BACK BUTTON
        btnBack.setOnClickListener(v -> finish());

        // DELETE SINGLE LOG BUTTON
        btnDelete.setOnClickListener(v -> {
            if (selectedPosition != -1 && waterLogInfos != null) {
                // Get the item using the selected index
                WaterLogInfo selectedWater = databaseHelper.getAllLogs(email).get(selectedPosition);

                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                loadHistoryData(); // Reload UI
            }
        });

        // CLEAR ALL LOGS BUTTON
        btnClearAll.setOnClickListener(v -> {

            Toast.makeText(this, "Cleared all water entries", Toast.LENGTH_SHORT).show();
            loadHistoryData(); // Reload UI
        });
    }

    private void setupNavigationListeners() {
        btnAcc.setOnClickListener(v -> {
            startActivity(new Intent(History.this, AccountView.class));
        });

        btnDashboard.setOnClickListener(v -> {
            startActivity(new Intent(History.this, Dashboard.class));
        });

    }
}