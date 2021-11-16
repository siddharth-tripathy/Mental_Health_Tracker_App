package com.example.mentalhealthtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity implements PaymentResultListener {
    Dialog dialog;
    Dialog doc_list;
    boolean payment=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dialog =new Dialog(Dashboard.this);
        dialog.setContentView(R.layout.subscription);

        doc_list =new Dialog(Dashboard.this);
        doc_list.setContentView(R.layout.doc_list);

        Button buy = dialog.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_m8Mx6M6wvVB1qu");
                //checkout.setImage(R.drawable.nev_cart);
                JSONObject object = new JSONObject();
                try {
                    object.put("name", "Sid");
                    //object.put("description", "Test Payment");
                    object.put("theme.color", "#FF8C00");
                    object.put("currency", "INR");
                    object.put("amount", 1000000);
                    object.put("prefill.contact", "919867425435");
                    object.put("prefill.email", "siddharth.tripathy01@gmail.com");
                    checkout.open(Dashboard.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        CardView analysis_option = findViewById(R.id.analysis_options);
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
            }
        });
        ImageView profile =findViewById(R.id.profileImage);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setVisibility(View.VISIBLE);
            }
        });
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
                startActivity(i);
            }
        });
        Button anxiety = findViewById(R.id.anxiety);
        anxiety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testFor = "Anxiety";
                Intent i = new Intent(Dashboard.this, Analysis.class);
                i.putExtra("AnalysisFor", testFor);
                startActivity(i);
            }
        });
        Button anger = findViewById(R.id.anger);
        anger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testFor = "Anger";
                Intent i = new Intent(Dashboard.this, Analysis.class);
                i.putExtra("AnalysisFor", testFor);
                startActivity(i);
            }
        });
        Button sleep = findViewById(R.id.sleep);
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testFor = "Sleep";
                Intent i = new Intent(Dashboard.this, Analysis.class);
                i.putExtra("AnalysisFor", testFor);
                startActivity(i);
            }
        });
        Button articles = findViewById(R.id.articles);
        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(Dashboard.this, Reads.class)); }
        });
        FloatingActionButton doc = findViewById(R.id.doc);
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (payment==false)
                //    dialog.show();
                //else
                    doc_list.show();
            }
        });
    }
    @Override
    public void onPaymentSuccess(String s) {
        dialog.hide();
        doc_list.show();
        payment=true;
    }
    @Override
    public void onPaymentError(int i, String s) { }
}