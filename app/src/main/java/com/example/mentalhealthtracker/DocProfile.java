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
import com.google.firebase.firestore.QuerySnapshot;
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
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DocProfile extends AppCompatActivity implements PaymentResultListener {

    TextView docName, temp, appointmentDate;
    ImageButton chat, video, call;
    String uName, docId, doc_Name, uNumber;
    LinearLayout contact;
    Button bkApp;
    String AppointmentDate;

    Button requestAppointment;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_profile);

        Intent i = getIntent();
        docId = i.getStringExtra("ID");
        doc_Name = i.getStringExtra("Name");

        docName = findViewById(R.id.DocName);
        docName.setText(docId);

        temp = findViewById(R.id.temp);
        temp.setText(doc_Name);

        appointmentDate = findViewById(R.id.AppointmentDate);

        contact = findViewById(R.id.contact);
        bkApp = findViewById(R.id.bkApp);

        chat = findViewById(R.id.chatBtn);
        call = findViewById(R.id.call);
        video = findViewById(R.id.videoCallBtn);

        requestAppointment = findViewById(R.id.requestAppointment);


        db.collection("DoctorUser").document(docId).collection("RequestList").document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            //requestAppointment.setVisibility(View.GONE);

                            if (documentSnapshot.exists()) {
                                call.setVisibility(View.VISIBLE);
                                AppointmentDate = documentSnapshot.getString("AppointmentDate");

                                if (AppointmentDate.equals("NotScheduled")){
                                    Log.d(TAG, "Appointment not scheduled!!!!!!");
                                    appointmentDate.setText("Not Scheduled");
                                    bkApp.setVisibility(View.GONE);
                                }
                                else {
                                    bkApp.setVisibility(View.VISIBLE);
                                    appointmentDate.setText(AppointmentDate);
                                    Log.d("TAG", "Appointment Date Set");
                                    String payment = documentSnapshot.getString("Payment");
                                    if (payment.equals("true")){
                                        Log.d("TAG", "Payment Complete");
                                        SimpleDateFormat Dt = new SimpleDateFormat("dd/MM/yyyy");
                                        Date aDt = null;
                                        try {
                                            aDt = Dt.parse(AppointmentDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        if (new Date().equals(aDt)){
                                            //check timestamp
                                            //if timestamp matches make video call button visible
                                            video.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            //Not Appointment date
                                            video.setVisibility(View.GONE);
                                        }
                                        bkApp.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        bkApp.setVisibility(View.VISIBLE);
                                    }
                                }


                                Log.d(TAG, "document does exist");
                                db.collection("DoctorUser").document(docId).collection("AppointmentList").document(currentUser)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()){
                                                        String validityDate = document.getString("Validity");
                                                        Log.d("TAG", "Validity Date" + validityDate);

                                                        SimpleDateFormat Dt = new SimpleDateFormat("dd/MM/yyyy");
                                                        Date vDt = null;
                                                        try {
                                                            vDt = Dt.parse(validityDate);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (new Date().after(vDt))
                                                        {
                                                            chat.setVisibility(View.GONE);
                                                            video.setVisibility(View.GONE);
                                                            requestAppointment.setVisibility(View.VISIBLE);
                                                            Log.d(TAG, "Crossed validity date");
                                                        }
                                                        else
                                                        {
                                                            Log.d("TAG", "Subscription Valid");
                                                            contact.setVisibility(View.VISIBLE);
                                                            chat.setVisibility(View.VISIBLE);
                                                            video.setVisibility(View.VISIBLE);
                                                            requestAppointment.setVisibility(View.GONE);
                                                        }
                                                    }
                                                    else {
                                                        Log.d(TAG, "Appointment not scheduled yet!!!!!!!");
                                                        requestAppointment.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        });
                            }
                            else {
                                Log.d(TAG, "Oopppssss!!!!");
                                requestAppointment.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });

        ////////////////////////Setting Name
        db.collection("DoctorUser").document(docId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (task.isSuccessful()){
                            doc_Name = documentSnapshot.getString("Name");
                            docName.setText(documentSnapshot.getString("Name"));
                        }
                    }
                });

        db.collection("User").document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            uName = documentSnapshot.getString("Name");
                            uNumber = documentSnapshot.getString("Number");
                        }
                    }
                });

        //////////////////////////Payment
        bkApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_m8Mx6M6wvVB1qu");
                //checkout.setImage(R.drawable.nev_cart);
                JSONObject object = new JSONObject();
                try {
                    object.put("name", uName);
                    //object.put("description", "Test Payment");
                    object.put("theme.color", "#FF8C00");
                    object.put("currency", "INR");
                    object.put("amount", 100000);
                    object.put("prefill.contact", uNumber);
                    checkout.open(DocProfile.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ////////////////////Contacting the Therapist
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DocProfile.this, Chat.class);
                intent.putExtra("SenderId", currentUser);
                intent.putExtra("ReceiverId", docId);
                intent.putExtra("Name", uName);
                startActivity(intent);
            }
        });

        requestAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> patientData = new HashMap<>();
                patientData.put("NameUser", uName);
                patientData.put("NameDoctor", doc_Name);
                patientData.put("Email", docId);
                patientData.put("PatientId", currentUser);
                patientData.put("AppointmentDate", "NotScheduled");
                patientData.put("Payment", "false");
                patientData.put("Approval", "pending");

                db.collection("User").document(currentUser).collection("RequestList").document(docId)
                        .set(patientData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });

                Task<Void> appointmentList = db.collection("DoctorUser").document(docId).collection("RequestList").document(currentUser)
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
        });
    }

    @Override
    public void onPaymentSuccess(String s) {

        db.collection("User").document(currentUser).collection("RequestList").document(docId)
                .update("Payment", "true")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "Payment status updated!!!");
                    }
                });

        db.collection("DoctorUser").document(docId).collection("RequestList").document(currentUser)
                .update("Payment", "true")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "Payment status updated!!!");
                    }
                });


        //payment=true;
        String  currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7); // Adding 5 days
        String output = sdf.format(c.getTime());

        Map<String, Object> patientData = new HashMap<>();
        patientData.put("NameUser", uName);
        patientData.put("NameDoctor", doc_Name);
        patientData.put("Email", docId);
        patientData.put("Time", currentDateTimeString);
        patientData.put("Validity", output);
        patientData.put("PatientId", currentUser);
        patientData.put("AppointmentDate", AppointmentDate);

        Task<Void> appointmentList = db.collection("DoctorUser").document(docId).collection("AppointmentList").document(currentUser)
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

        Task<Void> appointment = db.collection("User").document(currentUser).collection("AppointmentList").document(docId)
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

        chat.setVisibility(View.VISIBLE);
        video.setVisibility(View.VISIBLE);
        bkApp.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast toast = Toast.makeText(getApplicationContext(), "We are unable to process your request. Try Again", Toast.LENGTH_SHORT);
        toast.show();
    }
}