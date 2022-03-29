package com.example.mentalhealthtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class UserDoctorList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    RecyclerView DoctorPatientList;
    ArrayList<ModelDocList> modelArrayList;
    AdapterDocList myAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Spinner spinner_doc_list;
    CardView sam;
    ProgressDialog progressDialog;

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

        spinner_doc_list = findViewById(R.id.filterDL);

        ////////////////////////Chat Options Spinner
        //spinner_chat = (Spinner) findViewById(R.id.spinner_chat);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.spinner_doc_lst, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_doc_list.setAdapter(adapterC);
        spinner_doc_list.setOnItemSelectedListener(this);

        DoctorPatientList = findViewById(R.id.DoctorList);
        DoctorPatientList.setHasFixedSize(true);
        DoctorPatientList.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        modelArrayList = new ArrayList<>();
        myAdapter = new AdapterDocList(this, modelArrayList);
        DoctorPatientList.setAdapter(myAdapter);



        /*
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

         */

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String choice = parent.getItemAtPosition(pos).toString();

        if (choice.equals("--FILTER--")){
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
                            progressDialog.dismiss();
                        }
                    });

        }
        else if (choice.equals("All")){
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
                            progressDialog.dismiss();
                        }
                    });

        }
        else if (choice.equals("General")){
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").whereEqualTo("Bio", "General").orderBy("Name", Query.Direction.ASCENDING)
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
                            progressDialog.dismiss();
                        }
                    });

        }
        else if (choice.equals("Psychiatrist")) {
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").whereEqualTo("Bio", "Psychiatrist").orderBy("Name", Query.Direction.ASCENDING)
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
                            progressDialog.dismiss();
                        }
                    });
        }
        else if (choice.equals("Cognitive therapy")) {
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").whereEqualTo("Bio", "Cognitive therapy").orderBy("Name", Query.Direction.ASCENDING)
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
                            progressDialog.dismiss();
                        }
                    });
        }
        else if (choice.equals("Behavior therapy")) {
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").whereEqualTo("Bio", "Behavior therapy").orderBy("Name", Query.Direction.ASCENDING)
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
                            progressDialog.dismiss();
                        }
                    });
        }
        else if (choice.equals("Integrative therapy")) {
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").whereEqualTo("Bio", "Integrative therapy").orderBy("Name", Query.Direction.ASCENDING)
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
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}