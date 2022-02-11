package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

    TextView docName, temp, appointmentDate, requestAppointment, requestAppointmentCancel;
    TextView dBio, dLocation, dExp, dPatients;
    ImageButton chat, video, call;
    String uName, docId, doc_Name, uNumber, docBio;
    LinearLayout contact, appDt;
    Button bkApp;
    String AppointmentDate, AppointmentTime;
    CardView requestAppointmentMsg;
    CoordinatorLayout coordinatorLayout;
    Button requestAppointmentBtn;
    ImageView docProfileImg;
    MediaPlayer mediaPlayer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String bio, exp, location, patients, profileImg;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_profile);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Intent i = getIntent();
        docId = i.getStringExtra("ID");
        doc_Name = i.getStringExtra("Name");
        docBio = i.getStringExtra("Bio");

        docName = findViewById(R.id.DocName);
        docName.setText(docId);

        temp = findViewById(R.id.temp);
        temp.setText(bio);

        dLocation = findViewById(R.id.loc);
        dExp = findViewById(R.id.exp);
        dPatients = findViewById(R.id.totalPatients);
        docProfileImg = findViewById(R.id.docProfileImg);

        appointmentDate = findViewById(R.id.AppointmentDate);
        appDt = findViewById(R.id.appdt);

        contact = findViewById(R.id.contact);
        bkApp = findViewById(R.id.bkApp);

        chat = findViewById(R.id.chatBtn);
        call = findViewById(R.id.call);
        video = findViewById(R.id.videoCallBtn);

        requestAppointment = findViewById(R.id.requestAppointment);
        requestAppointmentBtn = findViewById(R.id.requestAppointmentBtn);
        requestAppointmentCancel = findViewById(R.id.requestAppointmentCancel);
        requestAppointmentMsg = findViewById(R.id.requestAppointmentMsg);

        db.collection("DoctorUser").document(docId).collection("RequestList").document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            //requestAppointment.setVisibility(View.GONE);

                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "Request is Sent!!!!");
                                requestAppointment.setVisibility(View.GONE);
                                appDt.setVisibility(View.VISIBLE);
                                call.setVisibility(View.VISIBLE);
                                AppointmentDate = documentSnapshot.getString("AppointmentDate");

                                if (AppointmentDate.equals("NotScheduled")) {
                                    Log.d(TAG, "Appointment not scheduled!!!!!");
                                    appointmentDate.setText("Not Scheduled");
                                    bkApp.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                } else {
                                    Log.d("TAG", "Appointment Scheduled");
                                    appointmentDate.setText(AppointmentDate);
                                    String payment = documentSnapshot.getString("Payment");
                                    AppointmentTime = documentSnapshot.getString("Time");

                                    if (payment.equals("true")) {
                                        Log.d("TAG", "Payment Complete");
                                        bkApp.setVisibility(View.GONE);

                                        db.collection("User").document(currentUser).collection("RequestList").document(docId)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                String validityDate = document.getString("Validity");
                                                                Log.d("TAG", "Validity Date" + validityDate);

                                                                SimpleDateFormat x = new SimpleDateFormat("dd/MM/yyyy");
                                                                Date vDt = null;
                                                                try {
                                                                    vDt = x.parse(validityDate);
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                if (new Date().after(vDt)) {
                                                                    chat.setVisibility(View.GONE);
                                                                    video.setVisibility(View.GONE);
                                                                    requestAppointment.setVisibility(View.VISIBLE);
                                                                    Log.d(TAG, "Crossed validity date");



                                                                    db.collection("DoctorUser").document(docId).collection("RequestList").document(currentUser)
                                                                            .delete()
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(@NonNull Void unused) {
                                                                                    Log.d("TAG", "Successfully Deleted");
                                                                                    Intent intent = new Intent(DocProfile.this, DocProfile.class);
                                                                                    intent.putExtra("ID", docId);
                                                                                    intent.putExtra("Name", doc_Name);
                                                                                    intent.putExtra("Bio", bio);
                                                                                    startActivity(intent);
                                                                                }
                                                                            })
                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Log.d("TAG", "Not Deleted");
                                                                                }
                                                                            });
                                                                } else {
                                                                    Log.d("TAG", "Subscription Valid");
                                                                    contact.setVisibility(View.VISIBLE);
                                                                    chat.setVisibility(View.VISIBLE);
                                                                    requestAppointment.setVisibility(View.GONE);
                                                                    progressDialog.dismiss();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                        Date c = Calendar.getInstance().getTime();
                                        SimpleDateFormat df = new SimpleDateFormat("MMM dd", Locale.getDefault());
                                        String currentDate = df.format(c);

                                        if (currentDate.compareTo(AppointmentDate)==0) {
                                            //check timestamp
                                            //if timestamp matches make video call button visible
                                            Log.d("TAG", "Yesss!!! Video Call"+AppointmentDate);
                                            video.setVisibility(View.VISIBLE);
                                            progressDialog.dismiss();
                                        }
                                        else {
                                            Log.d("TAG", "No Video Call"+AppointmentDate+currentDate);
                                            video.setVisibility(View.GONE);
                                            progressDialog.dismiss();
                                        }

                                        bkApp.setVisibility(View.GONE);
                                    } else {

                                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
                                        Date aDt = null;
                                        try {
                                            aDt = sdf.parse(AppointmentDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("Tag", "Payment not complete!!!!");
                                        if (new Date().after(aDt)) {
                                            bkApp.setVisibility(View.VISIBLE);
                                        } else {
                                            bkApp.setVisibility(View.GONE);
                                            requestAppointment.setVisibility(View.VISIBLE);
                                        }
                                        progressDialog.dismiss();
                                    }
                                }
                            } else {
                                Log.d(TAG, "Oopppssss!!!!");
                                requestAppointmentBtn.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });

        db.collection("DoctorUser").document(docId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()){
                            bio = documentSnapshot.getString("Bio");
                            temp.setText(bio);
                            location = documentSnapshot.getString("Location");
                            dLocation.setText(location);
                            patients = documentSnapshot.getString("TotalPatients");
                            dPatients.setText(patients);
                            exp = documentSnapshot.getString("Experience");
                            dExp.setText(exp);
                            profileImg = documentSnapshot.getString("Profileimage");
                            Glide.with(DocProfile.this)
                                    .load(profileImg)
                                    .placeholder(R.drawable.profile)
                                    .into(docProfileImg);
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
                        if (task.isSuccessful()) {
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
                        if (task.isSuccessful()) {
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
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa", Locale.getDefault());
                String currentDate = df.format(c);

                Date date = null;
                try {
                    date = df.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long TimeMilli = date.getTime();
                String timeStamp = String.valueOf(TimeMilli);
                Log.w("TAG", "Successful Written Data" + AppointmentDate+" "+AppointmentTime + timeStamp);


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

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_CALL);
                String number = "123456789";
                intentCall.setData(Uri.parse("tel:"+number));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    Snackbar snackbar = Snackbar.make(v, "Please grant permission!!!", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.failure));
                    snackbar.setTextColor(getResources().getColor(R.color.white));
                    snackbar.show();
                    requestPermission();
                }
                else {
                    startActivity(intentCall);
                }
            }
        });

        try {
            // object creation of JitsiMeetConferenceOptions
            // class by the name of options
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL(""))
                    .setWelcomePageEnabled(false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        requestAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAppointmentMsg.setVisibility(View.VISIBLE);
                requestAppointmentBtn.setVisibility(View.GONE);
            }
        });

        requestAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm aaa", Locale.getDefault());
                String currentDate = df.format(c);

                Date date = null;
                try {
                    date = df.parse(currentDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long TimeMilli = date.getTime();
                String timeStamp = String.valueOf(TimeMilli);

                Map<String, Object> patientData = new HashMap<>();
                patientData.put("NameUser", uName);
                patientData.put("NameDoctor", doc_Name);
                patientData.put("Email", docId);
                patientData.put("PatientId", currentUser);
                patientData.put("AppointmentDate", "NotScheduled");
                patientData.put("Payment", "false");
                patientData.put("Approval", "pending");
                patientData.put("RequestTime", timeStamp);

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

                db.collection("DoctorUser").document(docId).collection("RequestList").document(currentUser)
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

                requestAppointmentMsg.setVisibility(View.GONE);
                //Context contextView = findViewById(R.id.context_view)
                Snackbar snackbar = Snackbar.make(v, "Request Sent!!!", Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.teal_700));
                snackbar.show();
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        View v = findViewById(R.id.bkApp);
        Snackbar snackbar = Snackbar.make(v, "PAYMENT SUCCESSFUL!!!", Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.success));
        snackbar.setTextColor(getResources().getColor(R.color.black));
        snackbar.show();

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

        //Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM dd h:mm a", Locale.getDefault());
        //String currentDate = df.format(c);

        Date date = null;
        try {
            date = df.parse(AppointmentDate+" "+AppointmentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long TimeMilli = date.getTime();
        String timeStamp = String.valueOf(TimeMilli);

        //payment=true;
        String currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7); // Adding 7 days
        String output = sdf.format(cal.getTime());

        Map<String, Object> patientData = new HashMap<>();
        patientData.put("NameUser", uName);
        patientData.put("NameDoctor", doc_Name);
        patientData.put("Email", docId);
        patientData.put("Time", currentDateTimeString);
        patientData.put("Validity", output);
        patientData.put("PatientId", currentUser);
        patientData.put("AppointmentDate", AppointmentDate);
        patientData.put("AppointmentTime", AppointmentTime);
        patientData.put("timeStamp", timeStamp);
        patientData.put("Payment", "true");

        db.collection("DoctorUser").document(docId).collection("AppointmentList")
                .add(patientData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                        Log.w("TAG", "Successful Written Data" + AppointmentDate+" "+AppointmentTime);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });

        db.collection("User").document(currentUser).collection("AppointmentList")
                .add(patientData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                        Log.w("TAG", "Successful Written Data");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });

        db.collection("DoctorUser").document(docId).collection("RequestList").document(currentUser)
                .set(patientData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "Request status updated!!!");
                    }
                });

        db.collection("User").document(currentUser).collection("RequestList").document(docId)
                .set(patientData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "Request status updated!!!");
                    }
                });

        chat.setVisibility(View.VISIBLE);
        bkApp.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPaymentError(int i, String s) {
        View v = findViewById(R.id.bkApp);
        Snackbar snackbar = Snackbar.make(v, "OOPS!!! THERE WAS AN ERROR. TRY AGAIN", Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.failure));
        snackbar.setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(DocProfile.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

    public void videoLaunch(View v) {
            JitsiMeetConferenceOptions options
                    = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(currentUser + " " + docId)
                    .setFeatureFlag("invite.enabled",false)
                    .build();
            JitsiMeetActivity.launch(this, options);
    }
}