package com.example.mentalhealthtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class Anjaneyasana extends AppCompatActivity {

    private TextToSpeech mtts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anjaneyasana);

        Intent i = getIntent();
        String t = i.getStringExtra("Title");
        String c = i.getStringExtra("Content");
        Button start = findViewById(R.id.start);
        TextView yogTitle = findViewById(R.id.yogTitle);
        yogTitle.setText(t);

        TextView yogCon = findViewById(R.id.yogCon);
        yogCon.setText(c);

        mtts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    mtts.setLanguage(Locale.ENGLISH);
                    start.setEnabled(true);
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtts.speak(c, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }
    @Override
    protected void onDestroy() {
        if(mtts!=null){
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }
}