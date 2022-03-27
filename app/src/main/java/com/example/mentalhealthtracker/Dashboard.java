package com.example.mentalhealthtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    private ImageView profileImg, settings;
    private CardView account, appDash;
    private TextView userName, docName, trackHistory, profile, greetings, wwu, articlesMore;
    private String n;

    //Graph
    private LineChart lineChart;

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CardView depression, anxiety, anger, sleep;
    private Button analysis, doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null){
            finish();
        }

        //DashboardTop
        profileImg =findViewById(R.id.profileImage);
        settings = findViewById(R.id.sett);

        analysis = findViewById(R.id.analysis);

        appDash = findViewById(R.id.appDash);

        //TextView
        userName = findViewById(R.id.userName);
        profile = findViewById(R.id.profile);
        greetings = findViewById(R.id.greetings);
        wwu = findViewById(R.id.wwu);
        articlesMore = findViewById(R.id.articlesMore);

        //Analysis list
        depression = findViewById(R.id.Depression);
        anxiety = findViewById(R.id.Anxiety);
        anger = findViewById(R.id.Anger);
        sleep = findViewById(R.id.Sleep);

        //More Options
        trackHistory = findViewById(R.id.trackHistory);

        //Line Chart
        lineChart = findViewById(R.id.lineChart);

        db.collection("User").document(currentUser).collection("DDepression")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            lineChart.setDragEnabled(true);
                            lineChart.setScaleEnabled(true);

                            ArrayList<Entry> yValues = new ArrayList<>();

                            int i = 1;

                            //List<String> list = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String s = documentSnapshot.getString("DDepression");

                                assert s != null;
                                yValues.add(new Entry(i, Float.parseFloat(s)));
                                i++;
                            }

                            LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

                            set1.setFillAlpha(110);

                            //set1.setCircleColor(R.drawable.gradient_drawable);
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
        //sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

        depression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depression.setBackground(getResources().getDrawable(R.drawable.border));
                anxiety.setBackground(getResources().getDrawable(R.drawable.no_border));
                anger.setBackground(getResources().getDrawable(R.drawable.no_border));
                //sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

                db = FirebaseFirestore.getInstance();

                db.collection("User").document(currentUser).collection("DDepression")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    lineChart.setDragEnabled(true);
                                    lineChart.setScaleEnabled(true);

                                    ArrayList<Entry> yValues = new ArrayList<>();

                                    int i = 1;

                                    //List<String> list = new ArrayList<>();
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        String s = documentSnapshot.getString("DDepression");

                                        assert s != null;
                                        yValues.add(new Entry(i, Float.parseFloat(s)));
                                        i++;
                                    }

                                    LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");

                                    set1.setFillAlpha(110);

                                    //set1.setCircleColor(R.drawable.gradient_drawable);
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
       //         sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

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
           //     sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

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

        /*
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   sleep.setBackground(getResources().getDrawable(R.drawable.border));
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


         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal1 = Calendar.getInstance();
        //cal1.add(Calendar.DATE, 0); // Adding 7 days
        String output = sdf.format(cal1.getTime());

        Date date2 = null;
        try {
            date2 = sdf.parse(output);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long time = date2.getTime();
        String timeStampV = String.valueOf(time);

        db.collection("User").document(currentUser).collection("AppointmentList").whereGreaterThanOrEqualTo("timeStampV", timeStampV)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot.isEmpty()){
                                appDash.setVisibility(View.GONE);
                            }
                            else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                }
                            }
                        }
                    }
                });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Settings.class));
            }
        });

        trackHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, TrackHistory.class));
            }
        });

        wwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "example@exampl.com"));
                startActivity(intent);
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
                                n = document.getString("Name");
                                String profileUrl = document.getString("ProfileImageUrl");
                                if (profileUrl != null) {
                                    Glide.with(Dashboard.this)
                                            .load(profileUrl)
                                            .placeholder(R.drawable.profile)
                                            .into(profileImg);
                                }
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
            greetings.setText("Good Evening");
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
                db.collection("User").document(currentUser)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists())
                                {
                                    n = documentSnapshot.getString("Number");
                                }
                            }
                        });

                Intent intent = new Intent(Dashboard.this, CreateAccount.class);
                intent.putExtra("EditMode", "false");
                intent.putExtra("From", "dashboard");
                intent.putExtra("Number", n);
                startActivity(intent);
            }
        });
        account = findViewById(R.id.account);

        ImageView articles = findViewById(R.id.articles);
        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dashboard.this.startActivity(new Intent(Dashboard.this, Reads.class));
            }
        });
        doc = findViewById(R.id.doc);
        /*
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, DocList.class);
                i.putExtra("Name", n);
                startActivity(i);
            }
        });
         */

        articlesMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Articles.class));
            }
        });
    }
}
