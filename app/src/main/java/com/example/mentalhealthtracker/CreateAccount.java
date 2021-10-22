package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText name, email, age, moto;
    Button save;
    String eName, eEmail, eAge, eMoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        name = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextEmailAddress);
        age  = findViewById(R.id.editTextNumber);
        moto = findViewById(R.id.moto);

        save =findViewById(R.id.save);

        final String TAG = "MyActivity";

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eName = name.getText().toString();
                eEmail = email.getText().toString();
                eAge = age.getText().toString();
                eMoto = moto.getText().toString();

                Map<String, Object> accountData = new HashMap<>();
                accountData.put("name", eName);
                accountData.put("email", eEmail);
                accountData.put("age", eAge);
                accountData.put("moto", eMoto);

                Task<Void> account_data = db.collection(currentUser).document("Account Data")
                        .set(accountData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                startActivity(new Intent(CreateAccount.this, Dashboard.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                        /*
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                startActivity(new Intent(CreateAccount.this, Dashboard.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                         */
            }
        });

    }
}