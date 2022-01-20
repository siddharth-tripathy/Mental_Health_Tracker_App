package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TrackHistory extends AppCompatActivity {


    BarChart barChart;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_history);

        firebaseFirestore = FirebaseFirestore.getInstance();

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore.collection("User").document(currentUser)
                .collection("DDepression")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            barChart = findViewById(R.id.barChart);

                            ArrayList<BarEntry> visitors = new ArrayList<>();

                            int i = 1;

                            List<String> list = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String s = documentSnapshot.getString("DDepression");

                                visitors.add(new BarEntry(i, Float.parseFloat(s)));
                                i++;

                            }

                            BarDataSet barDataSet = new BarDataSet(visitors, "Visitors");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(16f);

                            BarData barData = new BarData(barDataSet);

                            barChart.setFitBars(true);
                            barChart.setData(barData);
                            barChart.getDescription().setText("Bar Chart Example");
                            barChart.animateY(2000);
                        }
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


