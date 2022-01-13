package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class DocList extends AppCompatActivity {

    CardView sam;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String docUid = "0qPs6bVRiWheAMec8no2qo9Wuah2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_list);

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

    }
}