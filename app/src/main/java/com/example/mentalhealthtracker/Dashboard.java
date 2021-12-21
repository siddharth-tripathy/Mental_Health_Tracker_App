package com.example.mentalhealthtracker;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Dashboard extends AppCompatActivity implements PaymentResultListener {
    Dialog dialog;
    Dialog doc_list;
    boolean payment=false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String docUid = "0qPs6bVRiWheAMec8no2qo9Wuah2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dialog =new Dialog(Dashboard.this);
        dialog.setContentView(R.layout.subscription);
        
        doc_list =new Dialog(Dashboard.this);
        doc_list.setContentView(R.layout.doc_list);

        Button buy = dialog.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                }
            }
        });

        CardView analysis_option = findViewById(R.id.analysis_options);
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        ImageView profile =findViewById(R.id.profileImage);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setVisibility(View.VISIBLE);
            }
        });
        Button analysis = findViewById(R.id.analysis);
        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analysis_option.setVisibility(View.VISIBLE);
            }
        });

        Button depression = findViewById(R.id.depression);
        depression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testFor = "DDepression";
                Intent i = new Intent(Dashboard.this, Analysis.class);
                i.putExtra("AnalysisFor", testFor);
                Dashboard.this.startActivity(i);
            }
        });
        Button anxiety = findViewById(R.id.anxiety);
        anxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testFor = "Anxiety";
                Intent i = new Intent(Dashboard.this, Analysis.class);
                i.putExtra("AnalysisFor", testFor);
                Dashboard.this.startActivity(i);
            }
        });
        Button anger = findViewById(R.id.anger);
        anger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testFor = "Anger";
                Intent i = new Intent(Dashboard.this, Analysis.class);
                i.putExtra("AnalysisFor", testFor);
                Dashboard.this.startActivity(i);
            }
        });
        Button sleep = findViewById(R.id.sleep);
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testFor = "Sleep";
                Intent i = new Intent(Dashboard.this, Analysis.class);
                i.putExtra("AnalysisFor", testFor);
                Dashboard.this.startActivity(i);
            }
        });
        Button articles = findViewById(R.id.articles);
        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dashboard.this.startActivity(new Intent(Dashboard.this, Reads.class));
            }
        });
        FloatingActionButton doc = findViewById(R.id.doc);
        doc.setOnClickListener(new View.OnClickListener() {
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
                                String validity = document.getString("Validity");

                                LocalDate dt = LocalDate.parse(validity);
                                LocalDate today = LocalDate.now();

                                LocalDate a = LocalDate.of(dt.getYear(), dt.getMonth(), dt.getDayOfMonth());
                                LocalDate b = LocalDate.of(today.getYear(), today.getMonth(), today.getDayOfMonth());
                                int x = b.compareTo(a);

                                if (x>=0)
                                {
                                    startActivity(new Intent(Dashboard.this, Chat.class));
                                }
                                else
                                {
                                    dialog.show();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPaymentSuccess(String s) {

        payment=true;
        String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0); // Adding 5 days
        String output = sdf.format(c.getTime());

        Map<String, Object> patientData = new HashMap<>();
        patientData.put("Name(User)", "Peter");
        patientData.put("Name(Doctor)", "Dr. Tony");
        patientData.put("Email", "peter@spiderman.com");
        patientData.put("Time", currentDateTimeString);
        patientData.put("Validity", output);
        patientData.put("PatientId", currentUser);

        Task<Void> appointmentList = db.collection("DoctorUser").document(docUid).collection("AppointmentList").document(currentUser)
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

        Task<Void> appointment = db.collection("User").document(currentUser).collection("Appointment").document(docUid)
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
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast toast = Toast.makeText(getApplicationContext(), "We are unable to process your request. Try Again", Toast.LENGTH_SHORT);
        toast.show();
    }

}
