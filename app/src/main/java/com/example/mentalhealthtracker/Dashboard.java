package com.example.mentalhealthtracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
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
    private ImageView profileImg, settings, article1, artcle2, artcle3, docP;
    private CardView account, appDash;
    private TextView userName, docName, trackHistory, profile, greetings, wwu, articlesMore, dcnm, addt, vddt;
    private String n, docProfileDP, ad, vd, nm, iddd;

    //Graph
    private LineChart lineChart;

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CardView depression, anxiety, anger, sleep;
    private Button analysis, doc, docPButton;

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

        docP = findViewById(R.id.docProfileDP);

        docPButton = findViewById(R.id.docPButton);

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

        addt = findViewById(R.id.addt);
        vddt = findViewById(R.id.vddt);
        dcnm = findViewById(R.id.dcnm);

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

        db.collection("User").document(currentUser).collection("AppointmentList").whereGreaterThanOrEqualTo("ValidityTimeStamp", timeStampV)
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
                                    appDash.setVisibility(View.VISIBLE);
                                    Log.d("TAG", document.getId() + " => " + document.getData()+timeStampV);
                                    String idd = document.getId();
                                    db.collection("User").document(currentUser).collection("AppointmentList").document(idd)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        nm = documentSnapshot.getString("NameDoctor");
                                                        vd = documentSnapshot.getString("Validity");
                                                        ad = documentSnapshot.getString("AppointmentDate");
                                                        docProfileDP = documentSnapshot.getString("DocImg");
                                                        iddd = documentSnapshot.getString("Email");

                                                        dcnm.setText(nm);
                                                        addt.setText(ad);
                                                        vddt.setText(vd);

                                                        Glide.with(Dashboard.this)
                                                                .load(docProfileDP)
                                                                .placeholder(R.drawable.profile)
                                                                .into(docP);
                                                    }
                                                }
                                            });
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
        Glide.with(Dashboard.this)
                .load("https://firebasestorage.googleapis.com/v0/b/mental-health-tracker-bf577.appspot.com/o/Reads%2FWhat%20Is%20Mental%20Health.jpg?alt=media&token=4cde2048-e470-4a54-9c54-efea0c2b05a5")
                .placeholder(R.drawable.profile)
                .into(articles);
        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Dashboard.this, Reads.class);
                a.putExtra("Name", "What is Mental Health");
                a.putExtra("URL", "https://firebasestorage.googleapis.com/v0/b/mental-health-tracker-bf577.appspot.com/o/Reads%2FWhat%20Is%20Mental%20Health.jpg?alt=media&token=4cde2048-e470-4a54-9c54-efea0c2b05a5");
                a.putExtra("ID", "RZVCKdSjf1XnZJMvioIJ");
                startActivity(a);
            }
        });

        artcle2 = findViewById(R.id.articles2);
        Glide.with(Dashboard.this)
                .load("https://firebasestorage.googleapis.com/v0/b/mental-health-tracker-bf577.appspot.com/o/Reads%2FCognitive%20behavioral%20therapy.jpeg?alt=media&token=9d4ef02b-5231-4f09-b3e4-bbf6cee1ff07")
                .placeholder(R.drawable.profile)
                .into(artcle2);
        artcle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Dashboard.this, Reads.class);
                a.putExtra("Name", "Cognitive behavioral therapy");
                a.putExtra("URL", "https://firebasestorage.googleapis.com/v0/b/mental-health-tracker-bf577.appspot.com/o/Reads%2FCognitive%20behavioral%20therapy.jpeg?alt=media&token=9d4ef02b-5231-4f09-b3e4-bbf6cee1ff07");
                a.putExtra("ID", "2g406B7Oa5J8LeKJmisD");
                startActivity(a);
            }
        });

        artcle3 = findViewById(R.id.articles3);
        Glide.with(Dashboard.this)
                .load("https://firebasestorage.googleapis.com/v0/b/mental-health-tracker-bf577.appspot.com/o/Reads%2F31%20Tips%20To%20Boost%20Your%20Mental%20Health.jfif?alt=media&token=cb44e2d6-b780-4f66-ac64-8664db701c07")
                .placeholder(R.drawable.profile)
                .into(artcle3);
        artcle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Dashboard.this, Reads.class);
                a.putExtra("Name", "31 Tips To Boost Your Mental Health");
                a.putExtra("URL", "https://firebasestorage.googleapis.com/v0/b/mental-health-tracker-bf577.appspot.com/o/Reads%2F31%20Tips%20To%20Boost%20Your%20Mental%20Health.jfif?alt=media&token=cb44e2d6-b780-4f66-ac64-8664db701c07");
                a.putExtra("ID", "RZVCKdSjf1XnZJMvioIJ");
                startActivity(a);
            }
        });

        doc = findViewById(R.id.doc);

        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, UserDoctorList.class);
                i.putExtra("Name", n);
                startActivity(i);
            }
        });

        articlesMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Articles.class));
            }
        });

        docPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this, DocProfile.class);
                i.putExtra("Name", nm);
                i.putExtra("ID", iddd);
                startActivity(i);
            }
        });
    }
}
