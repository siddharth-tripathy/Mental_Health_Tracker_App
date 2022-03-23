package com.example.mentalhealthtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AdapterRequestList extends RecyclerView.Adapter<AdapterRequestList.MyViewHolder> {

    Context context;
    ArrayList<ModelRequestList> requestLists;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdapterRequestList(Context context, ArrayList<ModelRequestList> requestLists) {
        this.context = context;
        this.requestLists = requestLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_list_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelRequestList model = requestLists.get(position);

        String name = model.NameUser;
        //String time = model.Time;
        String id = model.PatientId;

        holder.ModelRequestListName.setText(name);
        //holder.ModelPatientListTime.setText(time);
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
                                            .into(holder.ModelRequestProfileImg);
                                }
                            } else {

                            }
                        }

                    }
                });

        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(context, DPatientDetails.class);
                a.putExtra("Name", name);
                //a.putExtra("Time",time);
                a.putExtra("ID", id);
                a.putExtra("By", "request");
                a.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ModelRequestListName;
        ImageView ModelRequestProfileImg;
        Button viewMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ModelRequestListName = itemView.findViewById(R.id.requestName);
            ModelRequestProfileImg = itemView.findViewById(R.id.requestProfileImg);
            viewMore = itemView.findViewById(R.id.viewMore);
        }
    }
}
