package com.example.mentalhealthtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class Loader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(500);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally{
                    finish();
                }
            }
        };thread.start();
    }
}