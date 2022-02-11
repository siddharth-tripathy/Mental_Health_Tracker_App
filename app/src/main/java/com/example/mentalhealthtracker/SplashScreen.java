package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(6000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
                    if(firebaseUser != null)
                    {
                        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        db.collection("User").document(currentUser)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()){
                                            startActivity(new Intent(SplashScreen.this, Dashboard.class));
                                        }
                                        else{
                                            Intent i = new Intent(SplashScreen.this, ManageOtp.class);
                                            startActivity(i);
                                        }
                                        finish();
                                    }
                                });
                    }
                    else
                    {
                        Intent intent = new Intent(SplashScreen.this, ManageOtp.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        thread.start();
    }
}