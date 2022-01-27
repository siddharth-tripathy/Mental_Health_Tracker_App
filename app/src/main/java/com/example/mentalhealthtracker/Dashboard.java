package com.example.mentalhealthtracker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Dashboard extends AppCompatActivity {

    private ImageView profileImg, settings;
    private CardView account, yourTherapist;
    private TextView userName, docName, trackHistory, profile, greetings;


    //Graph
    private LineChart lineChart;


    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    private Dialog dialog;
    private Dialog doc_list;

    private boolean payment=false;
    private String docUid = "0qPs6bVRiWheAMec8no2qo9Wuah2";

    private CardView depression, anxiety, anger, sleep;

    private Button analysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //DashboardTop
        profileImg =findViewById(R.id.profileImage);
        settings = findViewById(R.id.settings);

        analysis = findViewById(R.id.analysis);


        //TextView
        userName = findViewById(R.id.userName);
        profile = findViewById(R.id.profile);
        greetings = findViewById(R.id.greetings);


        //Analysis list
        depression = findViewById(R.id.Depression);
        anxiety = findViewById(R.id.Anxiety);
        anger = findViewById(R.id.Anger);
        sleep = findViewById(R.id.Sleep);

        //More Options
        trackHistory = findViewById(R.id.trackHistory);

        //Line Chart
        lineChart = findViewById(R.id.lineChart);

      //  lineChart.setOnChartGestureListener(Dashboard.this);
      //  lineChart.setOnChartValueSelectedListener(Dashboard.this);

        db.collection("User").document(currentUser)
                .collection("Anxiety")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            lineChart.setDragEnabled(true);
                            lineChart.setScaleEnabled(true);

                            ArrayList<Entry> yValues = new ArrayList<>();

                            int i = 1;

                            List<String> list = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String s = documentSnapshot.getString("DDepression");

                                yValues.add(new Entry(i, Float.parseFloat(s)));
                                i++;
                            }

                            LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

                            set1.setFillAlpha(110);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(set1);

                            LineData data = new LineData(dataSets);
                            lineChart.setData(data);
                        }
                    }
                });

        depression.setBackground(getResources().getDrawable(R.drawable.border));
        anxiety.setBackground(getResources().getDrawable(R.drawable.no_border));
        anger.setBackground(getResources().getDrawable(R.drawable.no_border));
        sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

        depression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depression.setBackground(getResources().getDrawable(R.drawable.border));
                anxiety.setBackground(getResources().getDrawable(R.drawable.no_border));
                anger.setBackground(getResources().getDrawable(R.drawable.no_border));
                sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

                db = FirebaseFirestore.getInstance();

                db.collection("User").document(currentUser)
                        .collection("DDepression")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    lineChart.setDragEnabled(true);
                                    lineChart.setScaleEnabled(true);

                                    ArrayList<Entry> yValues = new ArrayList<>();

                                    int i = 1;

                                    List<String> list = new ArrayList<>();
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        String s = documentSnapshot.getString("DDepression");

                                        yValues.add(new Entry(i, Float.parseFloat(s)));
                                        i++;
                                    }

                                    LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

                                    set1.setFillAlpha(110);

                                    set1.setCircleColor(Color.BLUE);
                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);

                                    LineData data = new LineData(dataSets);
                                    lineChart.setData(data);
                                }
                            }
                        });

                startActivity(new Intent(Dashboard.this, Loader.class));

                analysis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String testFor = "DDepression";
                        Intent i = new Intent(Dashboard.this, Analysis.class);
                        i.putExtra("AnalysisFor", testFor);
                        Dashboard.this.startActivity(i);
                    }
                });
            }
        });

        anxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anxiety.setBackground(getResources().getDrawable(R.drawable.border));
                depression.setBackground(getResources().getDrawable(R.drawable.no_border));
                anger.setBackground(getResources().getDrawable(R.drawable.no_border));
                sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

                db.collection("User").document(currentUser)
                        .collection("Anxiety")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    lineChart.setDragEnabled(true);
                                    lineChart.setScaleEnabled(true);

                                    ArrayList<Entry> yValues = new ArrayList<>();

                                    int i = 1;

                                    List<String> list = new ArrayList<>();
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        String s = documentSnapshot.getString("Anxiety");

                                        yValues.add(new Entry(i, Float.parseFloat(s)));
                                        i++;

                                    }

                                    LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

                                    set1.setFillAlpha(110);

                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);

                                    LineData data = new LineData(dataSets);
                                    lineChart.setData(data);
                                }
                            }
                        });

                startActivity(new Intent(Dashboard.this, Loader.class));

                analysis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String testFor = "Anxiety";
                        Intent i = new Intent(Dashboard.this, Analysis.class);
                        i.putExtra("AnalysisFor", testFor);
                        Dashboard.this.startActivity(i);
                    }
                });
            }
        });

        anger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anger.setBackground(getResources().getDrawable(R.drawable.border));
                anxiety.setBackground(getResources().getDrawable(R.drawable.no_border));
                depression.setBackground(getResources().getDrawable(R.drawable.no_border));
                sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

                db.collection("User").document(currentUser)
                        .collection("Anger")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    lineChart.setDragEnabled(true);
                                    lineChart.setScaleEnabled(true);

                                    ArrayList<Entry> yValues = new ArrayList<>();

                                    int i = 1;

                                    List<String> list = new ArrayList<>();
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        String s = documentSnapshot.getString("Anger");

                                        yValues.add(new Entry(i, Float.parseFloat(s)));
                                        i++;
                                    }

                                    LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

                                    set1.setFillAlpha(110);

                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);

                                    LineData data = new LineData(dataSets);
                                    lineChart.setData(data);
                                }
                            }
                        });

                startActivity(new Intent(Dashboard.this, Loader.class));

                analysis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String testFor = "Anger";
                        Intent i = new Intent(Dashboard.this, Analysis.class);
                        i.putExtra("AnalysisFor", testFor);
                        Dashboard.this.startActivity(i);
                    }
                });
            }
        });

        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sleep.setBackground(getResources().getDrawable(R.drawable.border));
                anxiety.setBackground(getResources().getDrawable(R.drawable.no_border));
                anger.setBackground(getResources().getDrawable(R.drawable.no_border));
                depression.setBackground(getResources().getDrawable(R.drawable.no_border));

                db.collection("User").document(currentUser)
                        .collection("Sleep")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    lineChart.setDragEnabled(true);
                                    lineChart.setScaleEnabled(true);

                                    ArrayList<Entry> yValues = new ArrayList<>();

                                    int i = 1;

                                    List<String> list = new ArrayList<>();
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        String s = documentSnapshot.getString("Sleep");

                                        yValues.add(new Entry(i, Float.parseFloat(s)));
                                        i++;
                                    }

                                    LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

                                    set1.setFillAlpha(110);

                                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                    dataSets.add(set1);

                                    LineData data = new LineData(dataSets);
                                    lineChart.setData(data);
                                }
                            }
                        });

                startActivity(new Intent(Dashboard.this, Loader.class));

                analysis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String testFor = "Sleep";
                        Intent i = new Intent(Dashboard.this, Analysis.class);
                        i.putExtra("AnalysisFor", testFor);
                        Dashboard.this.startActivity(i);
                    }
                });
            }
        });

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
                intent.putExtra("From", "dashboard");
                startActivity(intent);
            }
        });
        account = findViewById(R.id.account);



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