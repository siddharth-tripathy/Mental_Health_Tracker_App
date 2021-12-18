package com.example.mentalhealthtracker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements PaymentResultListener {
    Dialog dialog;
    Dialog doc_list;
    boolean payment=false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dialog =new Dialog(Dashboard.this);
        dialog.setContentView(R.layout.subscription);
        
        doc_list =new Dialog(Dashboard.this);
        doc_list.setContentView(R.layout.doc_list);

        Button buy = dialog.findViewById(R.id.buy);
        buy.setOnClickListener(v -> {
            /*
            String docUid = "0qPs6bVRiWheAMec8no2qo9Wuah2";
            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            String paymentConfirmation = db.collection("DoctorUser").document(docUid).collection("PatientList").document(currentUser).collection("Payment")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                        }
                    });

            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_m8Mx6M6wvVB1qu");
            //checkout.setImage(R.drawable.nev_cart);
            JSONObject object = new JSONObject();
            try {
                object.put("name", "Sid");
                //object.put("description", "Test Payment");
                object.put("theme.color", "#FF8C00");
                object.put("currency", "INR");
                object.put("amount", 1000000);
                object.put("prefill.contact", "919867425435");
                object.put("prefill.email", "siddharth.tripathy01@gmail.com");
                checkout.open(Dashboard.this, object);
            } catch (JSONException e) {
                e.printStackTrace();
             */
        });

        CardView analysis_option = findViewById(R.id.analysis_options);
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> FirebaseAuth.getInstance().signOut());
        ImageView profile =findViewById(R.id.profileImage);
        profile.setOnClickListener(v -> logout.setVisibility(View.VISIBLE));
        Button analysis = findViewById(R.id.analysis);
        analysis.setOnClickListener(v -> analysis_option.setVisibility(View.VISIBLE));

        Button depression = findViewById(R.id.depression);
        depression.setOnClickListener(v -> {
            String testFor = "DDepression";
            Intent i = new Intent(Dashboard.this, Analysis.class);
            i.putExtra("AnalysisFor", testFor);
            startActivity(i);
        });
        Button anxiety = findViewById(R.id.anxiety);
        anxiety.setOnClickListener(v -> {
            String testFor = "Anxiety";
            Intent i = new Intent(Dashboard.this, Analysis.class);
            i.putExtra("AnalysisFor", testFor);
            startActivity(i);
        });
        Button anger = findViewById(R.id.anger);
        anger.setOnClickListener(v -> {
            String testFor = "Anger";
            Intent i = new Intent(Dashboard.this, Analysis.class);
            i.putExtra("AnalysisFor", testFor);
            startActivity(i);
        });
        Button sleep = findViewById(R.id.sleep);
        sleep.setOnClickListener(v -> {
            String testFor = "Sleep";
            Intent i = new Intent(Dashboard.this, Analysis.class);
            i.putExtra("AnalysisFor", testFor);
            startActivity(i);
        });
        Button articles = findViewById(R.id.articles);
        articles.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, Reads.class)));
        FloatingActionButton doc = findViewById(R.id.doc);
        doc.setOnClickListener(v -> {
            //if (payment==false)
              //  dialog.show();
            //else
                //doc_list.show();
            startActivity(new Intent(Dashboard.this, Chat.class));
        });
/*
        ImageView chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, Chat.class);
                i.putExtra("Name", "Dr. Tony");
                startActivity(i);
            }
        });
          */
    }

    @Override
    public void onPaymentSuccess(String s) {

        payment=true;
        String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 20); // Adding 5 days
        String output = sdf.format(c.getTime());

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> patientData = new HashMap<>();
        patientData.put("Name", "Peter");
        patientData.put("Email", "peter@peter.com");
        patientData.put("Payment", "true");
        patientData.put("Time", currentDateTimeString);
        patientData.put("Date", output);
        patientData.put("PatientId", currentUser);

        String docUid = "0qPs6bVRiWheAMec8no2qo9Wuah2";

        Task<Void> account_data = db.collection("DoctorUser").document(docUid).collection("PatientList").document(currentUser)
                .set(patientData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
        //Date dt2 = new Date().getTime();
        //String date = sdf.format(new Date());
        //if (date.equals(output))
        //{
        //}
    }

    @Override
    public void onPaymentError(int i, String s) {
    }

    public void dateCheck(String startDate) {

    }
}