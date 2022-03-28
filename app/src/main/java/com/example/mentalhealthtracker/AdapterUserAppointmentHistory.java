package com.example.mentalhealthtracker;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterUserAppointmentHistory extends RecyclerView.Adapter<AdapterUserAppointmentHistory.MyViewHolder> {

    Context context;
    ArrayList<ModelUserAppoinmentHistory> modelArrayList;

    public AdapterUserAppointmentHistory(Context context, ArrayList<ModelUserAppoinmentHistory> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_user_appointment_history, parent, false);
        return new AdapterUserAppointmentHistory.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelUserAppoinmentHistory model = modelArrayList.get(position);
        String name = model.NameDoctor;
        String ts= model.timeStamp;
        String bkDt= model.Time;
        String dt= model.AppointmentDate;
        String tm= model.AppointmentTime;

        holder.userAppDocName.setText(name);
        holder.userAppTime.setText(tm);
        holder.userAppDate.setText(dt);
        holder.userAppBookDate.setText(bkDt);

        Log.d(TAG, name+ts+bkDt);

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userAppBookDate, userAppDocName, userAppDate, userAppTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userAppDate = itemView.findViewById(R.id.userAppDate);
            userAppBookDate = itemView.findViewById(R.id.userAppBookDate);
            userAppTime = itemView.findViewById(R.id.userAppTime);
            userAppDocName = itemView.findViewById(R.id.userAppDocName);
        }
    }
}
