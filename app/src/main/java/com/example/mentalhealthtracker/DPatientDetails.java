package com.example.mentalhealthtracker;

import static android.content.ContentValues.TAG;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DPatientDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button scheduleNow, ok, timeButton, confirm;
    TextView appointmentDate, uName, timeTextView, dob, gen;
    CardView chat;
    String appDt, pName, doc_Name, id, appTime, d, g;
    Spinner spinner;
    LinearLayout date, contact, dateSetter, timeSetter;
    List<String> categories;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Calendar calendar1;
    ImageView profile;
    String profileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_patient_details);

        Intent i = getIntent();
        id = i.getStringExtra("ID");
        String by = i.getStringExtra("By");

        scheduleNow = findViewById(R.id.ScheduleNow);
        appointmentDate = findViewById(R.id.AppointmentDate);
        chat = findViewById(R.id.chatBtn);
        contact = findViewById(R.id.contact);
        spinner = findViewById(R.id.date);
        date = findViewById(R.id.dateLayout);
        dateSetter = findViewById(R.id.DateSetter);
        timeSetter = findViewById(R.id.TimeSetter);
        confirm = findViewById(R.id.confirm);
        uName = findViewById(R.id.name);
        timeTextView = findViewById(R.id.timeTextView);
        timeButton = findViewById(R.id.timeButton);
        dob = findViewById(R.id.dob);
        gen = findViewById(R.id.gen);
        profile = findViewById(R.id.profile_image);

        db.collection("User").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (task.isSuccessful()) {
                            pName = documentSnapshot.getString("Name");
                            uName.setText(pName);
                            d = documentSnapshot.getString("DateOfBirth");
                            dob.setText(d);
                            g = documentSnapshot.getString("Gender");
                            gen.setText(g);

                            profileUrl = documentSnapshot.getString("ProfileImageUrl");
                            if (profileUrl != null) {
                                Glide.with(DPatientDetails.this)
                                        .load(profileUrl)
                                        .into(profile);
                            }


                        }
                    }
                });

        db.collection("DoctorUser").document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (task.isSuccessful()) {
                            doc_Name = documentSnapshot.getString("Name");
                        }
                    }
                });

        dateSet();

        db.collection("DoctorUser").document(currentUser).collection("RequestList").document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            appDt = documentSnapshot.getString("AppointmentDate");
                            Log.d("TAG", appDt);
                            appointmentDate.setText(appDt);
                            if (appDt.equals("NotScheduled")) {
                                Log.d("TAG", "Appointment Not Scheduled");
                                scheduleNow.setText("Schedule now");
                                contact.setVisibility(View.GONE);
                            } else {
                                Log.d("TAG", "Appointment Scheduled");
                                scheduleNow.setVisibility(View.GONE);
                                contact.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            Log.d("TAG", id);
                            scheduleNow.setVisibility(View.VISIBLE);
                            contact.setVisibility(View.GONE);
                        }
                    }
                });

        scheduleNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSetter.setVisibility(View.VISIBLE);
                timeSetter.setVisibility(View.VISIBLE);
                //dateSet.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);
                scheduleNow.setVisibility(View.GONE);
            }
        });

        // Spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");

        // Spinner Drop down elements
        categories = new ArrayList<String>();

        categories.add("--Select Date--");

        for (int d = 1; d < 8; d++) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, d); // Adding days
            String output = sdf.format(c.getTime());
            categories.add(output);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DPatientDetails.this, DDoctorPatientchat.class);
                i.putExtra("Name", pName);
                i.putExtra("ProfileUrl", profileUrl);
                i.putExtra("ID", id);
                startActivity(i);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                Log.d("TAG", "On Click Listener Running");

                appDt = appointmentDate.getText().toString();
                appTime = timeTextView.getText().toString();

                Map<String, Object> requestData =new HashMap<>();
                requestData.put("Name(User)", pName);
                requestData.put("Name(Doctor)", doc_Name);
                requestData.put("Email", currentUser);
                requestData.put("PatientId", id);
                requestData.put("AppointmentDate", appDt);
                requestData.put("AppointmentTime", appTime);
                requestData.put("Payment", "false");
                requestData.put("Approval", "Approved");
                requestData.put("Time", dateText);

                db.collection("User").document(id).collection("RequestList").document(currentUser)
                        .set(requestData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                                scheduleNow.setVisibility(View.GONE);
                                confirm.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Request Data written");
                                scheduleNow.setVisibility(View.VISIBLE);
                            }
                        });

                db.collection("DoctorUser").document(currentUser).collection("RequestList").document(id)
                        .set(requestData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Log.d("TAG", "Request Data written");
                                scheduleNow.setVisibility(View.GONE);
                                confirm.setVisibility(View.GONE);
                                dateSetter.setVisibility(View.GONE);
                                timeSetter.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                                scheduleNow.setVisibility(View.VISIBLE);
                            }
                        });
            }
        });
    }

    public void dateSet() {
        appDt = appointmentDate.getText().toString();
        if (appDt.equals("Not Scheduled")) {
            scheduleNow.setText("Schedule Now");
        } else if (appDt.equals("--Select--")) {
            scheduleNow.setText("Schedule Now");
        } else {
            scheduleNow.setVisibility(View.GONE);
            appointmentDate.setTextColor(getResources().getColor(R.color.teal_700));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String dt = adapterView.getItemAtPosition(i).toString();
        appointmentDate.setText(dt);
        scheduleNow.setVisibility(View.VISIBLE);
        dateSet();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        scheduleNow.setText("Schedule Now");
        scheduleNow.setVisibility(View.VISIBLE);
        date.setVisibility(View.GONE);
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                timeTextView.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);
        timePickerDialog.show();
    }

    public void videoLaunch(View v) {
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom(id + " " + currentUser)
                .build();
        JitsiMeetActivity.launch(this, options);
    }
}