package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DocProfile extends AppCompatActivity implements PaymentResultListener {

    ImageButton chat;
    TextView temp;
    String validity;
    LinearLayout contact;
    Button bkApp;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String docUid = "gUeAyAHY0WcnKJlJGywK0rW0aN13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_profile);

        Intent i = getIntent();
        validity = i.getStringExtra("validity");

        contact = findViewById(R.id.contact);

        bkApp = findViewById(R.id.bkApp);

        if (validity.equals("false"))
        {
            contact.setVisibility(View.INVISIBLE);
            bkApp.setVisibility(View.VISIBLE);
        }
        else
        {
            contact.setVisibility(View.VISIBLE);
            bkApp.setVisibility(View.INVISIBLE);
        }

        chat = findViewById(R.id.chatBtn);
        temp = findViewById(R.id.temp);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocProfile.this, Chat.class));
            }
        });

        bkApp.setOnClickListener(new View.OnClickListener() {
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
                        checkout.open(DocProfile.this, object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        //payment=true;
        String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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

        contact.setVisibility(View.VISIBLE);
        bkApp.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast toast = Toast.makeText(getApplicationContext(), "We are unable to process your request. Try Again", Toast.LENGTH_SHORT);
        toast.show();
    }
}