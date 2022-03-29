package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class Settings extends AppCompatActivity {
    TextView tH, wwu, logout, ph, name, aph, abtus;
    ImageView profileImg, SettingsBackBtn;
    String n;
    CardView profile;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsBackBtn = findViewById(R.id.SettingsBackBtn);
        SettingsBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        abtus = findViewById(R.id.au);
        abtus.setMovementMethod(LinkMovementMethod.getInstance());

        aph = findViewById(R.id.ah);
        aph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, UserAppointmentHistory.class));
            }
        });

        name = findViewById(R.id.name);
        profileImg = findViewById(R.id.profile_image);
        db.collection("User").document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                String n = document.getString("Name");
                                String p = document.getString("ProfileImageUrl");
                                name.setText(n);
                                Glide.with(Settings.this)
                                        .load(p)
                                        .placeholder(R.drawable.profile)
                                        .into(profileImg);
                            }
                        }
                    }
                });

        ph = findViewById(R.id.ph);
        //tH = findViewById(R.id.trackHistory);
        ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, PaymentHistory.class));
            }
        });

        /*
        wwu = findViewById(R.id.wwu);
        wwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "example@exampl.com"));
                startActivity(intent);
            }
        });

         */
        /*
        tH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, TrackHistory.class));
            }
        });
         */
        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("User").document(currentUser)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists())
                                {
                                    n = documentSnapshot.getString("Number");
                                    Intent intent = new Intent(Settings.this, CreateAccount.class);
                                    intent.putExtra("EditMode", "false");
                                    intent.putExtra("From", "dashboard");
                                    intent.putExtra("Number", n);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });

        TextView deleteAcc = findViewById(R.id.deleteAcc);
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "example@example.com"));
                startActivity(intent);
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Settings.this, SplashScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}