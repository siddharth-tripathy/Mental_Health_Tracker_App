package com.example.mentalhealthtracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Calendar;
import java.util.Date;

public class DocMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView DoctorPatientList, RequestList, DoctorPatientAllChatList;
    ArrayList<ModelDoctorPatientList> modelArrayList;
    ArrayList<ModelRequestList> requestLists;
    ArrayList<ModelChatList> modelChatLists;
    AdapterChatList adapterChatList;
    AdapterDoctorPatientList myAdapter;
    AdapterRequestList requestAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String selectedChat, selectedApp;
    Spinner spinner_chat, spinner_appointment;
    TextView userName;
    ImageView settings;
    TextView chatList, appointmentList, requestList;
    LinearLayout appList, reqlist, chtList;
    CardView spinner_chat_options, spinner_appointment_options;
    Boolean chatTab , sessionTab;
    String timeStampYear;
    String timeStampMonth, timeStampDay, timeStampWeek;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main);


        db.collection("DoctorUser").document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            String a = documentSnapshot.getString("Approval");
                            if (a.equals("false")){
                                startActivity(new Intent(DocMainActivity.this, Approval.class));
                                finish();
                            }
                        }
                    }
                });


        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String Uid = mUser.getUid();

        chatList = findViewById(R.id.chatList);
        appointmentList = findViewById(R.id.appointmentList);
        requestList = findViewById(R.id.request_List);

        appList = findViewById(R.id.appList);
        reqlist = findViewById(R.id.reqList);
        chtList = findViewById(R.id.chtList);

        spinner_chat_options = findViewById(R.id.spinner_chat_options);
        spinner_appointment_options = findViewById(R.id.spinner_appointment_options);

        ////////////////////////Chat Options Spinner
        spinner_chat = (Spinner) findViewById(R.id.spinner_chat);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.chat_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_chat.setAdapter(adapterC);
        spinner_chat.setOnItemSelectedListener(this);


        //////////////////////////Appointment Options Spinner
        spinner_appointment = (Spinner) findViewById(R.id.spinner_appointment);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterA = ArrayAdapter.createFromResource(this, R.array.app_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_appointment.setAdapter(adapterA);
        spinner_appointment.setOnItemSelectedListener(this);
        selectedApp  = spinner_appointment.getSelectedItem().toString();
        selectedChat  = spinner_chat.getSelectedItem().toString();

        chatList.setBackgroundColor(0x30000000);
        appointmentList.setBackgroundColor(0x00000000);
        requestList.setBackgroundColor(0x00000000);

        appList.setVisibility(View.GONE);
        reqlist.setVisibility(View.GONE);
        chtList.setVisibility(View.VISIBLE);

        spinner_chat_options.setVisibility(View.VISIBLE);
        spinner_appointment_options.setVisibility(View.GONE);

        chatTab = true;
        sessionTab = false;

        chatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatList.setBackgroundColor(0x30000000);
                appointmentList.setBackgroundColor(0x00000000);
                requestList.setBackgroundColor(0x00000000);

                appList.setVisibility(View.GONE);
                reqlist.setVisibility(View.GONE);
                chtList.setVisibility(View.VISIBLE);

                spinner_chat_options.setVisibility(View.VISIBLE);
                spinner_appointment_options.setVisibility(View.GONE);

                chatTab = true;
                sessionTab = false;
            }
        });

        appointmentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatList.setBackgroundColor(0x00000000);
                appointmentList.setBackgroundColor(0x30000000);
                requestList.setBackgroundColor(0x00000000);

                appList.setVisibility(View.VISIBLE);
                reqlist.setVisibility(View.GONE);
                chtList.setVisibility(View.GONE);

                spinner_chat_options.setVisibility(View.GONE);
                spinner_appointment_options.setVisibility(View.VISIBLE);

                chatTab = false;
                sessionTab = true;
            }
        });

        requestList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatList.setBackgroundColor(0x00000000);
                appointmentList.setBackgroundColor(0x00000000);
                requestList.setBackgroundColor(0x30000000);

                appList.setVisibility(View.GONE);
                reqlist.setVisibility(View.VISIBLE);
                chtList.setVisibility(View.GONE);

                spinner_chat_options.setVisibility(View.GONE);
                spinner_appointment_options.setVisibility(View.GONE);
            }
        });

        Date date = null;

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd h:mm a");
        Calendar c = Calendar.getInstance();













        //////////////////////////////////////Day
        c.add(Calendar.DATE, -1);
        String outputD = sdf.format(c.getTime());

        try {
            date = sdf.parse(outputD);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long TimeMilliD = date.getTime();
        timeStampDay = String.valueOf(TimeMilliD);






        //////////////////////////////////////week
        c.add(Calendar.DATE, -8);
        String outputW = sdf.format(c.getTime());

        try {
            date = sdf.parse(outputW);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long TimeMilliW = date.getTime();
        timeStampWeek = String.valueOf(TimeMilliW);


        //////////////////////////////////////Monthly
        c.add(Calendar.DATE, -31);
        String outputM = sdf.format(c.getTime());

        try {
            date = sdf.parse(outputM);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long TimeMilliM = date.getTime();
        timeStampMonth = String.valueOf(TimeMilliM);


        ////////////////////////////////////////Year
        c.add(Calendar.DATE, -365);
        String outputY = sdf.format(c.getTime());
        try {
            date = sdf.parse(outputY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long TimeMilliY = date.getTime();
        timeStampYear = String.valueOf(TimeMilliY);

        DoctorPatientList = findViewById(R.id.DoctorPatientList);
        DoctorPatientList.setHasFixedSize(true);
        DoctorPatientList.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        modelArrayList = new ArrayList<>();

        myAdapter = new AdapterDoctorPatientList(this, modelArrayList);
        DoctorPatientList.setAdapter(myAdapter);

        RequestList = findViewById(R.id.requestList);
        RequestList.setHasFixedSize(true);
        RequestList.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        requestLists = new ArrayList<>();

        requestAdapter = new AdapterRequestList(this, requestLists);
        RequestList.setAdapter(requestAdapter);

        requestLists.clear();
        requestAdapter.notifyDataSetChanged();

        firebaseFirestore.collection("DoctorUser").document(currentUser).collection("RequestList").whereEqualTo("AppointmentDate", "NotScheduled").orderBy("RequestTime", Query.Direction.ASCENDING)//.whereEqualTo("Status", "Approved").whereEqualTo("ShopVisibility", "Visible").orderBy("ShopName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                requestLists.add(dc.getDocument().toObject(ModelRequestList.class));
                            }
                            requestAdapter.notifyDataSetChanged();
                        }
                    }
                });































        DoctorPatientAllChatList = findViewById(R.id.DoctorPatientAllChatList);
        DoctorPatientAllChatList.setHasFixedSize(true);
        DoctorPatientAllChatList.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();
        modelChatLists = new ArrayList<>();

        adapterChatList = new AdapterChatList(this, modelChatLists);
        DoctorPatientAllChatList.setAdapter(adapterChatList);













        //logout
        settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DocMainActivity.this, DocSettings.class));
            }
        });

        userName = findViewById(R.id.userName);
        db.collection("DoctorUser").document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                String n = document.getString("Name");
                                userName.setText(n);
                            }
                        }
                    }
                });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String choice = parent.getItemAtPosition(pos).toString();

        if (choice.equals("Upcoming")){
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").document(currentUser).collection("AppointmentList").whereGreaterThanOrEqualTo("timeStamp", timeStampDay).orderBy("timeStamp", Query.Direction.ASCENDING)  //.whereEqualTo("Status", "Approved").whereEqualTo("ShopVisibility", "Visible").orderBy("ShopName", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore Error", error.getMessage());
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    modelArrayList.add(dc.getDocument().toObject(ModelDoctorPatientList.class));
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        }
                    });

        }
        else if (choice.equals("Past 7 Days")){
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").document(currentUser).collection("AppointmentList").whereGreaterThanOrEqualTo("timeStamp", timeStampWeek).orderBy("timeStamp", Query.Direction.ASCENDING)  //.whereEqualTo("Status", "Approved").whereEqualTo("ShopVisibility", "Visible").orderBy("ShopName", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore Error", error.getMessage());
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    modelArrayList.add(dc.getDocument().toObject(ModelDoctorPatientList.class));
                                }
                                myAdapter.notifyDataSetChanged();

                            }
                            progressDialog.dismiss();
                        }
                    });

        }
        else if (choice.equals("Past 30 Days")){
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").document(currentUser).collection("AppointmentList").whereGreaterThanOrEqualTo("timeStamp", timeStampMonth).orderBy("timeStamp", Query.Direction.ASCENDING)  //.whereEqualTo("Status", "Approved").whereEqualTo("ShopVisibility", "Visible").orderBy("ShopName", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore Error", error.getMessage());
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    modelArrayList.add(dc.getDocument().toObject(ModelDoctorPatientList.class));
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        else if (choice.equals("Past Year")){
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").document(currentUser).collection("AppointmentList").whereGreaterThanOrEqualTo("timeStamp", timeStampYear)//.whereEqualTo("Status", "Approved").whereEqualTo("ShopVisibility", "Visible").orderBy("ShopName", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore Error", error.getMessage());
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    modelArrayList.add(dc.getDocument().toObject(ModelDoctorPatientList.class));
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        else if (choice.equals("All Time")){
            modelArrayList.clear();
            myAdapter.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").document(currentUser).collection("AppointmentList").orderBy("timeStamp", Query.Direction.ASCENDING)  //.whereEqualTo("Status", "Approved").whereEqualTo("ShopVisibility", "Visible").orderBy("ShopName", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore Error", error.getMessage());
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    modelArrayList.add(dc.getDocument().toObject(ModelDoctorPatientList.class));
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        else if (choice.equals("Unread")){
            modelChatLists.clear();
            adapterChatList.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").document(currentUser).collection("UnreadChatsList").orderBy("TimeStamp", Query.Direction.ASCENDING)  //.whereEqualTo("Status", "Approved").whereEqualTo("ShopVisibility", "Visible").orderBy("ShopName", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore Error", error.getMessage());
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    modelChatLists.add(dc.getDocument().toObject(ModelChatList.class));
                                }
                                adapterChatList.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
        else if (choice.equals("All Chats")){
            modelChatLists.clear();
            adapterChatList.notifyDataSetChanged();
            firebaseFirestore.collection("DoctorUser").document(currentUser).collection("AllChatsList").orderBy("TimeStamp", Query.Direction.ASCENDING)  //.whereEqualTo("Status", "Approved").whereEqualTo("ShopVisibility", "Visible").orderBy("ShopName", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                Log.e("Firestore Error", error.getMessage());
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                if (dc.getType() == DocumentChange.Type.ADDED) {
                                    modelChatLists.add(dc.getDocument().toObject(ModelChatList.class));
                                }
                                adapterChatList.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}