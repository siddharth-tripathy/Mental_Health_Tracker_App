package com.example.mentalhealthtracker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class Reads extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    CardView play;
    String Id, Name, Url;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView content, artName;
    ImageView arImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reads);

        Intent i = getIntent();
        Id = i.getStringExtra("ID");
        Name = i.getStringExtra("Name");
        Url = i.getStringExtra("URL");
        content = findViewById(R.id.content);
        artName = findViewById(R.id.ArticleName);
        arImg = findViewById(R.id.arImg);

        artName.setText(Name);

        Glide.with(Reads.this)
                .load(Url)
                .placeholder(R.drawable.profile)
                .into(arImg);

        Log.d(TAG, "The tag is......."+Id);

        db.collection("Reads").document(Id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot= task.getResult();
                            String con = documentSnapshot.getString("Content");
                            content.setText(con);
                        }
                    }
                });

        /*
        play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio("https://firebasestorage.googleapis.com/v0/b/mental-health-tracker-bf577.appspot.com/o/f4966aa0-89bf-11ec-be00-2df43e5f9fa9.mp3?alt=media&token=f9d22bc5-f3a0-4756-ac0a-456e957b92a2");
            }
        });
    }

    private void playAudio(String audioUrl) {
        if(haveNetwork()) {
            Toast.makeText(getApplicationContext(), "Loading Audio", Toast.LENGTH_LONG).show();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try{
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
        }

         */
    }

    private boolean haveNetwork(){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }
        return have_WIFI||have_MobileData;
    }
}