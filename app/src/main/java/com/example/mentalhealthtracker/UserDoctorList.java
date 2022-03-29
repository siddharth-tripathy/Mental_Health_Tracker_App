package com.example.mentalhealthtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserDoctorList extends AppCompatActivity {


    RecyclerView DoctorPatientList;
    ArrayList<ModelDocList> modelArrayList;
    AdapterDocList myAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    CardView sam;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String docUid;

    TextView nameUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_doctor_list);

        nameUser = findViewById(R.id.nameUser);

        Intent i = getIntent();

        nameUser.setText(i.getStringExtra("Name"));

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String Uid = mUser.getUid();

        DoctorPatientList = findViewById(R.id.DoctorList);
        DoctorPatientList.setHasFixedSize(true);
        DoctorPatientList.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        modelArrayList = new ArrayList<>();
        myAdapter = new AdapterDocList(this, modelArrayList);
        DoctorPatientList.setAdapter(myAdapter);

        modelArrayList.clear();
        myAdapter.notifyDataSetChanged();
        firebaseFirestore.collection("DoctorUser").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                modelArrayList.add(dc.getDocument().toObject(ModelDocList.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });

                /*
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                modelArrayList.add(dc.getDocument().toObject(ModelDocList.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });

                 */


    }
}