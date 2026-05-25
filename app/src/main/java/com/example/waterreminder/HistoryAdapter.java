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
        void onWaterClicked(WaterLogInfo water, int position);
    }

    private ArrayList<WaterLogInfo> waterLogInfos;
    private OnWaterClickListener listener;
    private int selectedPosition = -1;


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

        holder.tvHistoLog.setText(water.getWaterIntake() + " ml");
        holder.tvHistoTime.setText(water.getDateTime());

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION && listener != null) {
                selectedPosition = currentPosition;
                listener.onWaterClicked(water, currentPosition);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setSelected(selectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return waterLogInfos != null ? waterLogInfos.size() : 0;
    }

    public void updateData(ArrayList<WaterLogInfo> newList) {
        this.waterLogInfos = newList;
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvHistoLog, tvHistoTime;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoLog = itemView.findViewById(R.id.tvHistoLog);
            tvHistoTime = itemView.findViewById(R.id.tvHistoTime);
        }
    }
}