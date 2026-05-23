package com.example.waterreminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waterreminder.models.WaterLogInfo;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    public interface OnWaterClickListener {
        void onWaterClicked(WaterLogInfo water);
    }

    private final ArrayList<WaterLogInfo> waterLogInfos;
    private final OnWaterClickListener listener;

    public HistoryAdapter(ArrayList<WaterLogInfo> waterLogInfos, OnWaterClickListener listener) {
        this.waterLogInfos = waterLogInfos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        WaterLogInfo water = waterLogInfos.get(position);

        holder.tvHistoLog.setText(water.getWaterIntake());
        holder.tvHistoTime.setText(water.getDateTime());
    }


    @Override
    public int getItemCount() {
        return waterLogInfos.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvHistoLog, tvHistoTime;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoLog   = itemView.findViewById(R.id.tvHistoLog);
            tvHistoTime = itemView.findViewById(R.id.tvHistoTime);
        }
    }
}
