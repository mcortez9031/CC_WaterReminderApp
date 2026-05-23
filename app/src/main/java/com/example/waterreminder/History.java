package com.example.waterreminder;

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
    private Button btnClearAll, btnDelete;
    private MaterialButton btnBack;
    private HistoryAdapter historyAdapter;
    private ArrayList<WaterLogInfo> waterLogInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btn_delete);
        btnClearAll = findViewById(R.id.btn_clear_all);
        rvHistory = findViewById(R.id.rv_history);
        databaseHelper = new DatabaseHelper(this);

    }
}