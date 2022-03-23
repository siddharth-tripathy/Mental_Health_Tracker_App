package com.example.mentalhealthtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterDoctorPatientList extends RecyclerView.Adapter<AdapterDoctorPatientList.MyViewHolder> {

    Context context;
    ArrayList<ModelDoctorPatientList> modelArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdapterDoctorPatientList(Context context, ArrayList<ModelDoctorPatientList> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.patientlistlayout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelDoctorPatientList model = modelArrayList.get(position);

        String name = model.NameUser;
        String time = model.AppointmentDate;
        String id = model.PatientId;

        holder.ModelPatientListName.setText(name);
        holder.ModelPatientListTime.setText(time);

        db.collection("User").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                String n = document.getString("Name");
                                String profileUrl = document.getString("ProfileImageUrl");
                                if (profileUrl != null) {
                                    Glide.with(context)
                                            .load(profileUrl)
                                            .into(holder.ModelPatientProfile);
                                }
                            } else {

                            }
                        }

                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(context, DPatientDetails.class);
                a.putExtra("Name", name);
                a.putExtra("ID", id);
                a.putExtra("By", "appointment");
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ModelPatientListName, ModelPatientListTime;
        ImageView ModelPatientProfile;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ModelPatientListName = itemView.findViewById(R.id.ModelPatientListName);
            ModelPatientListTime = itemView.findViewById(R.id.ModelPatientListTime);
            ModelPatientProfile = itemView.findViewById(R.id.profileImage);
        }
    }
}
