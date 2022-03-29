package com.example.mentalhealthtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseUser;

public class Anjaneyasana extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anjaneyasana);

        VideoView videoView = findViewById(R.id.videoViewV);


        videoView.setVideoPath("https://firebasestorage.googleapis.com/v0/b/learnsanskrit-af209.appspot.com/o/Videos%2Fmyvowels.mp4?alt=media&token=19fe2a3e-bb30-4a24-b4e6-4b0b14c2d328");


        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        Button play = (Button) findViewById(R.id.playVidV);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), "Loading Video...", Toast.LENGTH_LONG).show();
                        videoView.start();

            }
        });
    }
}