package com.example.mentalhealthtracker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

//import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class CreateAccount extends AppCompatActivity {
    String editMode, uName, uEmail;
    TextView name, emailId, age, edit;
    EditText eName, eEmailId, eAge;
    LinearLayout eGender;
    Button save, gender, male, female, other;

    //Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        editMode = "true";

        //Intent i = getIntent();
        //editMode = i.getStringExtra("EditMode");

        //TextView
        name  = (TextView)findViewById(R.id.name);
        emailId = findViewById(R.id.emailId);
        edit = findViewById(R.id.edit);

        //EditText
        eName = findViewById(R.id.editName);
        eEmailId = findViewById(R.id.editEmailId);

        //LinearLayout
        eGender = findViewById(R.id.eGender);

        //Button
        save = findViewById(R.id.save);
        gender = findViewById(R.id.gender);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMode = "true";
            }
        });

        if (editMode.equals("true")) {
            name.setVisibility(View.GONE);
            emailId.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);

            eName.setVisibility(View.VISIBLE);
            eEmailId.setVisibility(View.VISIBLE);

            save.setVisibility(View.VISIBLE);
        }

        if (editMode.equals("false")) {
            db.collection("User").document(currentUser)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        String n = document.getString("name");
                        name.setText(n);
                    } else {
    
                    }
                }
            }
        });
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uName = eName.getText().toString();
                uEmail = eEmailId.getText().toString();

                Map<String, Object> accountData = new HashMap<>();
                accountData.put("name", "Siddharth");
                accountData.put("email", "sid@sid.com");
                accountData.put("age","20");
                accountData.put("gender","male");

                db.collection("User").document(currentUser)
                        .set(accountData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                                startActivity(new Intent(CreateAccount.this, Dashboard.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
            }
        });
    }
}