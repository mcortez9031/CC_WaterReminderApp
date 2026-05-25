package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private MaterialButton btnDelete, btnClearAll;
    private HistoryAdapter historyAdapter;
    private ArrayList<WaterLogInfo> waterLogInfos;
    private int selectedPosition = -1;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
        email = sharedPref.getString("email", "");

        btnDashboard = findViewById(R.id.btnDashboard);
        btnHisto = findViewById(R.id.btnHisto);
        btnAcc = findViewById(R.id.btnAcc);
        btnDelete = findViewById(R.id.btn_delete);
        btnClearAll = findViewById(R.id.btn_clear_all);
        rvHistory = findViewById(R.id.rv_history);

        databaseHelper = new DatabaseHelper(this);

        waterLogInfos = new ArrayList<>();

        historyAdapter = new HistoryAdapter(waterLogInfos, (water, position) -> {
            selectedPosition = position;
            btnDelete.setEnabled(true);
            Toast.makeText(History.this, "Selected: " + water.getWaterIntake() + "ml", Toast.LENGTH_SHORT).show();
        });

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(historyAdapter);

        btnDelete.setEnabled(false);

        setupNavigationListeners();
        setupActionListeners();

        loadHistoryData();
    }

    private void loadHistoryData() {
        waterLogInfos.clear();

        ArrayList<WaterLogInfo> freshLogs = databaseHelper.getAllLogs(email);
        if (freshLogs != null) {
            waterLogInfos.addAll(freshLogs);
        }

        historyAdapter.updateData(waterLogInfos);

        selectedPosition = -1;
        btnDelete.setEnabled(false);
    }

    private void setupActionListeners() {
        btnDelete.setOnClickListener(v -> {
            if (selectedPosition != -1 && selectedPosition < waterLogInfos.size()) {
                WaterLogInfo selectedWater = waterLogInfos.get(selectedPosition);

                boolean deleted = databaseHelper.deleteLog(selectedWater.getId());

                if (deleted) {
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                    loadHistoryData();
                } else {
                    Toast.makeText(this, "Failed to delete item from database", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClearAll.setOnClickListener(v -> {
            boolean cleared = databaseHelper.clearAllLogs(email);

            if (cleared) {
                Toast.makeText(this, "Cleared all water entries", Toast.LENGTH_SHORT).show();
                loadHistoryData();
            } else {
                Toast.makeText(this, "Failed to clear entries", Toast.LENGTH_SHORT).show();
            }
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