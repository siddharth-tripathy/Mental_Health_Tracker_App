package com.example.mentalhealthtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class Loader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        //Toast.makeText(Loader.this, "Loading...", Toast.LENGTH_SHORT).show();

        TextView quote = findViewById(R.id.quote);
        int x = 1;
        switch (x){
            case 1:
                quote.setText("“Happiness can be found even in the darkest of times, if one only remembers to turn on the light.”");
                break;
        }

        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally{
                    Random random = new Random();
                    //int x = random.nextInt(7);
                    finish();
                }
            }
        };thread.start();
    }
}