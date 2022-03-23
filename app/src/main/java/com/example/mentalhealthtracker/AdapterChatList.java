package com.example.mentalhealthtracker;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dropbox.core.v2.teamlog.SfInviteGroupDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.MyViewHolder> {
    Context context;
    ArrayList<ModelChatList> modelChatLists;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String n;
    String p;
    public AdapterChatList(Context context, ArrayList<ModelChatList> modelChatLists) {
        this.context = context;
        this.modelChatLists = modelChatLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.doctor_patient_chat_list_layout, parent, false);
        return new AdapterChatList.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelChatList model = modelChatLists.get(position);

        String By = model.By;
        String TimeStamp = model.TimeStamp;

        db.collection("User").document(By)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()){
                                n = documentSnapshot.getString("Name");
                                p = documentSnapshot.getString("ProfileImageUrl");

                                holder.PatientName.setText(n);
                                Glide.with(context)
                                        .load(p)
                                        .placeholder(R.drawable.profile)
                                        .into(holder.patientDp);
                            }
                        }
                    }
                });

        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent i = new Intent(context, DDoctorPatientchat.class);
                i.putExtra("Name", n);
                i.putExtra("ProfileUrl", p);
                i.putExtra("ID", By);
                context.startActivity(i);

                db.collection("DoctorUser").document(currentUser).collection("UnreadChatsList").document(By)
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "Deleted Successfully");
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelChatLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView PatientName;
        ImageView patientDp;
        Button viewMore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PatientName = itemView.findViewById(R.id.PatientName);
            patientDp = itemView.findViewById(R.id.patientDp);
            viewMore = itemView.findViewById(R.id.viewMore);
        }
    }
}
