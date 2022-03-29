package com.example.mentalhealthtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseUser;

public class Anjaneyasana extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anjaneyasana);

        Intent i = getIntent();
        String t = i.getStringExtra("Title");
        String c = i.getStringExtra("Content");

        TextView yogTitle = findViewById(R.id.yogTitle);
        yogTitle.setText(t);

        TextView yogCon = findViewById(R.id.yogCon);
        yogCon.setText(c);
    }
}