package com.example.mentalhealthtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterDoctorList extends RecyclerView.Adapter<AdapterDoctorList.MyViewHolder> {

    Context context;
    ArrayList<ModelDoctorList> modelArrayList;

    String validity;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public AdapterDoctorList(Context context, ArrayList<ModelDoctorList> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.doctor_list_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelDoctorList model = modelArrayList.get(position);

        String name = model.Name;
        String id = model.UID;

        holder.ModelDoctorListName.setText(name);
        holder.ModelDoctorListId.setText(id);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*
                SimpleDateFormat Dt = new SimpleDateFormat("dd/MM/yyyy");
                Date vDt = null;
                try {
                    vDt = Dt.parse(validityDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (new Date().after(vDt))
                {
                    //String validity="false";
                    Intent i = new Intent(DocList.this, DocProfile.class);
                    i.putExtra("validity", "false");
                    startActivity(i);
                }
                else
                {
                    //String validity="true";
                    Intent i = new Intent(DocList.this, DocProfile.class);
                    i.putExtra("validity", "true");
                    startActivity(i);
                }
                 */

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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ModelDoctorListName = itemView.findViewById(R.id.ModelDoctorListName);
            ModelDoctorListId = itemView.findViewById(R.id.ModelDoctorListId);
        }
    }
}
