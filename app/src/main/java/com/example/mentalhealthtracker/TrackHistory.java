package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TrackHistory extends AppCompatActivity {

    CardView Depression, Anger, Anxiety;
    BarChart barChart;
    FirebaseFirestore firebaseFirestore;
    String val="DDepression";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView dep, anx, ang;
    ArrayList<ModelTrackResultD> model;
    AdapterTrackResult myAdapter;

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_history);

        Depression = findViewById(R.id.Depression);
        Anger = findViewById(R.id.Anger);
        Anxiety = findViewById(R.id.Anxiety);
        firebaseFirestore = FirebaseFirestore.getInstance();

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        lineChart = findViewById(R.id.lineChart);

        Depression.setBackground(getResources().getDrawable(R.drawable.border));
        Anxiety.setBackground(getResources().getDrawable(R.drawable.no_border));
        Anger.setBackground(getResources().getDrawable(R.drawable.no_border));
        //sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

        Depression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Depression.setBackground(getResources().getDrawable(R.drawable.border));
                Anxiety.setBackground(getResources().getDrawable(R.drawable.no_border));
                Anger.setBackground(getResources().getDrawable(R.drawable.no_border));
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


                                    List<String> list = new ArrayList<>();
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        String s = documentSnapshot.getString("Result");

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

                startActivity(new Intent(TrackHistory.this, Loader.class));
            }
        });

        Anxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Anxiety.setBackground(getResources().getDrawable(R.drawable.border));
                Depression.setBackground(getResources().getDrawable(R.drawable.no_border));
                Anger.setBackground(getResources().getDrawable(R.drawable.no_border));
                //sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

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

                startActivity(new Intent(TrackHistory.this, Loader.class));

            }
        });

        Anger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Anger.setBackground(getResources().getDrawable(R.drawable.border));
                Anxiety.setBackground(getResources().getDrawable(R.drawable.no_border));
                Depression.setBackground(getResources().getDrawable(R.drawable.no_border));
                //sleep.setBackground(getResources().getDrawable(R.drawable.no_border));

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

                startActivity(new Intent(TrackHistory.this, Loader.class));

            }
        });
    }
    /*
    Report Generation

 firebaseFirestore.collection("User").document(currentUser)
                .collection("DDepression").whereGreaterThanOrEqualTo("Date", timestamp1).whereLessThanOrEqualTo("Date", timestamp2)
                .get()
     */
}


