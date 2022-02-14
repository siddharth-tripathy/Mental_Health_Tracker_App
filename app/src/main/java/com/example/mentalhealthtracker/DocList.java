package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class DocList extends AppCompatActivity {

    RecyclerView DoctorPatientList;
    ArrayList<ModelDoctorList> modelArrayList;
    AdapterDoctorList myAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    CardView sam;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String docUid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);


        Intent i = getIntent();
        TextView nameUser = findViewById(R.id.nameUser);
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
        myAdapter = new AdapterDoctorList(this, modelArrayList);
        DoctorPatientList.setAdapter(myAdapter);

        modelArrayList.clear();
        myAdapter.notifyDataSetChanged();
        firebaseFirestore.collection("DoctorUser").orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                modelArrayList.add(dc.getDocument().toObject(ModelDoctorList.class));
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                });
        /*
        sam = findViewById(R.id.dr_sameer);
        sam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("User").document(currentUser).collection("Appointment").document(docUid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot document = task.getResult();
                                    String validityDate = document.getString("Validity");

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

                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }

                                }
                            }
                        });
            }
        });

         */

    }
}