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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterDocList extends RecyclerView.Adapter<AdapterDocList.MyViewHolder> {

    Context context;
    ArrayList<ModelDocList> modelArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public AdapterDocList(Context context, ArrayList<ModelDocList> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_user_doctor_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelDocList model = modelArrayList.get(position);
        String name = model.Name;
        String id = model.UID;

        db.collection("DoctorUser").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String bio = documentSnapshot.getString("Bio");
                            holder.ModelDoctorListId.setText(bio);
                            String profileImg = documentSnapshot.getString("Profileimage");
                            Glide.with(context)
                                    .load(profileImg)
                                    .placeholder(R.drawable.profile)
                                    .into(holder.ModelDocDp);
                        }
                    }
                });


        holder.ModelDoctorListName.setText(name);

        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "The user id is ............:" + id);
                Intent a = new Intent(context, DocProfile.class);
                a.putExtra("Name", name);
                a.putExtra("ID", id);
                a.putExtra("Validity", "false");
                context.startActivity(a);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ModelDoctorListName, ModelDoctorListId;
        ImageView ModelDocDp;
        Button viewMore;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ModelDoctorListName = itemView.findViewById(R.id.ModelDoctorListName);
            ModelDoctorListId = itemView.findViewById(R.id.ModelDoctorListId);
            ModelDocDp = itemView.findViewById(R.id.docDp);
            viewMore = itemView.findViewById(R.id.viewMore);
        }
    }
}
