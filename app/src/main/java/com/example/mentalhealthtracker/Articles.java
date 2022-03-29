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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Articles extends AppCompatActivity {

    RecyclerView readList;
    ArrayList<ModelReadList> modelArrayList;
    AdapterReadList myAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageView SettingsBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String Uid = mUser.getUid();

        SettingsBackBtn = findViewById(R.id.SettingsBackBtn);
        SettingsBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        readList = findViewById(R.id.readList);
        readList.setHasFixedSize(true);
        readList.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        modelArrayList = new ArrayList<>();
        myAdapter = new AdapterReadList(this, modelArrayList);
        readList.setAdapter(myAdapter);

        modelArrayList.clear();
        myAdapter.notifyDataSetChanged();
        firebaseFirestore.collection("Reads").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                modelArrayList.add(dc.getDocument().toObject(ModelReadList.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}