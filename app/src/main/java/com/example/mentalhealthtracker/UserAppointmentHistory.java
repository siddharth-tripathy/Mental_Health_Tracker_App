package com.example.mentalhealthtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserAppointmentHistory extends AppCompatActivity {
    RecyclerView UserAppList;
    ArrayList<ModelUserAppoinmentHistory> modelArrayList;
    AdapterUserAppointmentHistory myAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment_history);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String Uid = mUser.getUid();

        UserAppList = findViewById(R.id.userAppointmentHistory);
        UserAppList.setHasFixedSize(true);
        UserAppList.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        modelArrayList = new ArrayList<>();
        myAdapter = new AdapterUserAppointmentHistory(this, modelArrayList);
        UserAppList.setAdapter(myAdapter);

        modelArrayList.clear();
        myAdapter.notifyDataSetChanged();
        firebaseFirestore.collection("User").document(currentUser).collection("AppointmentList").orderBy("NameDoctor", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                modelArrayList.add(dc.getDocument().toObject(ModelUserAppoinmentHistory.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}