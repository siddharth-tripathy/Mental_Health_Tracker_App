package com.example.mentalhealthtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AppointmentHistory extends AppCompatActivity {
    RecyclerView PaymentHistory;
    ArrayList<ModelPayment> modelPayment;
    AdapterPaymentHistory myAdapter;
    FirebaseFirestore firebaseFirestore;
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    ImageView UAppBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        PaymentHistory = findViewById(R.id.AppointmentHistory);
        PaymentHistory.setHasFixedSize(true);
        PaymentHistory.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        modelPayment = new ArrayList<>();
        myAdapter = new AdapterPaymentHistory(this, modelPayment);
        PaymentHistory.setAdapter(myAdapter);

        modelPayment.clear();
        myAdapter.notifyDataSetChanged();
        firebaseFirestore.collection("User").document(currentUser).collection("PaymentHistory").orderBy("PaidTo", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                modelPayment.add(dc.getDocument().toObject(ModelPayment.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });


    }
}