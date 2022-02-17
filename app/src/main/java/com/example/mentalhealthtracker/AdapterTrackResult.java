package com.example.mentalhealthtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterTrackResult extends RecyclerView.Adapter<AdapterTrackResult.MyViewHolder> {
    Context context;
    ArrayList<ModelTrackResultD> modelTrackHistory;

    public AdapterTrackResult(Context context, ArrayList<ModelTrackResultD> modelTrackHistory) {
        this.context = context;
        this.modelTrackHistory = modelTrackHistory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.depression_result_layout, parent, false);
        return new AdapterTrackResult.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelTrackResultD model  = modelTrackHistory.get(position);
        String Result = model.Result;
        String Date = model.Date;
        String DateTS  = model.DateTS;

        holder.resu.setText(Result);
        holder.resDate.setText(Date);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView resu, resDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            resu = itemView.findViewById(R.id.resu);
            resDate = itemView.findViewById(R.id.resDate);
        }
    }
}
