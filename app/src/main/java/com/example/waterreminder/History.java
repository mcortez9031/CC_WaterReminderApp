package com.example.waterreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterreminder.models.WaterLogInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;

public class History extends AppCompatActivity {

    private RecyclerView rvHistory;
    private DatabaseHelper databaseHelper;
    private Button btnDashboard, btnHisto, btnAcc;
    private MaterialButton btnBack, btnDelete, btnClearAll;
    private HistoryAdapter historyAdapter;
    private ArrayList<WaterLogInfo> waterLogInfos;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnDashboard = findViewById(R.id.btnDashboard);
        btnHisto = findViewById(R.id.btnHisto);
        btnAcc = findViewById(R.id.btnAcc);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btn_delete);
        btnClearAll = findViewById(R.id.btn_clear_all);
        rvHistory = findViewById(R.id.rv_history);
        databaseHelper = new DatabaseHelper(this);

        btnAcc.setOnClickListener(v -> {
            Intent intent = new Intent(History.this, AccountView.class);
            startActivity(intent);
        });
        btnDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(History.this, Dashboard.class);
            startActivity(intent);
        });
        btnHisto.setOnClickListener(v -> {
            Intent intent = new Intent(History.this, History.class);
            startActivity(intent);
        });

        setupRecyclerView();
    }
    private void setupRecyclerView() {
        waterLogInfos = databaseHelper.getAllLogs();
        if (waterLogInfos == null) {
            waterLogInfos = new ArrayList<>();
        }

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(historyAdapter);

        historyAdapter = new HistoryAdapter(waterLogInfos,
                new HistoryAdapter.OnItemClickListener(){

            }
        }
    }