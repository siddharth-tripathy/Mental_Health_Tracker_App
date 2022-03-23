package com.example.mentalhealthtracker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DPaymentHistory extends AppCompatActivity {

    RecyclerView PaymentHistory;
    ArrayList<ModelDPayment> modelPayment;
    AdapterDPaymentHistory myAdapter;
    FirebaseFirestore firebaseFirestore;
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_payment_history);

        PaymentHistory = findViewById(R.id.paymentHistory);
        PaymentHistory.setHasFixedSize(true);
        PaymentHistory.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        modelPayment = new ArrayList<>();
        myAdapter = new AdapterDPaymentHistory(this, modelPayment);
        PaymentHistory.setAdapter(myAdapter);

        modelPayment.clear();
        myAdapter.notifyDataSetChanged();
        firebaseFirestore.collection("DoctorUser").document(currentUser).collection("PaymentHistory").orderBy("PaidBy", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                modelPayment.add(dc.getDocument().toObject(ModelDPayment.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}