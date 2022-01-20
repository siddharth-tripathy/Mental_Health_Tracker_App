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
import com.google.firebase.firestore.Query;
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

public class Dashboard extends AppCompatActivity {

    ImageView profileImg, settings;
    CardView account, yourTherapist;
    TextView userName, docName, trackHistory, profile, greetings;



    //Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    Dialog dialog;
    Dialog doc_list;

    boolean payment=false;
    String docUid = "0qPs6bVRiWheAMec8no2qo9Wuah2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //DashboardTop
        profileImg =findViewById(R.id.profileImage);
        settings = findViewById(R.id.settings);

        //TextView
        userName = findViewById(R.id.userName);
        profile = findViewById(R.id.profile);
        greetings = findViewById(R.id.greetings);

        //More Options
        trackHistory = findViewById(R.id.trackHistory);






        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account.setVisibility(View.VISIBLE);
            }
        });

        trackHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, TrackHistory.class));
            }
        });

        //Getting User Name
        db.collection("User").document(currentUser)
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
                            } else {

                            }
                        }
                    }
                });

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if(hour>= 12 && hour < 17){
            greetings.setText("Good Afternoon");
        } else if(hour >= 17 && hour < 21){
            greetings.setText("Good Evening");
        } else if(hour >= 21 && hour < 24){
            greetings.setText("Good Night");
        } else {
            greetings.setText("Good Morning");

        }
















        yourTherapist = findViewById(R.id.yourTherapist);
        yourTherapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, DocProfile.class));
            }
        });

        CardView analysis_option = findViewById(R.id.analysis_options);
        TextView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, CreateAccount.class);
                intent.putExtra("editMode", "false");
                startActivity(intent);
            }
        });
        account = findViewById(R.id.account);

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
                startActivity(new Intent(Dashboard.this, DocList.class));
            }
        });
    }
}