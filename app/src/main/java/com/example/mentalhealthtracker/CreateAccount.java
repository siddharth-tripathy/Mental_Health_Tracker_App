package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private interface PictuteListener {
        void onProfilePictureUpdated();
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText name, email, age, moto;
    Button save;
    String eName, eEmail, eAge, eMoto;
    ImageView profile;
    private WebView.PictureListener pictureListener;
    FirebaseUser firebaseUser;

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





            }
        });

        /*
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        pictureListener = new PictuteListener() {
            @Override
            public void onProfilePictureUpdated() {
                Uri uri = firebaseUser.getPhotoUrl();
                Glide.with(getActivity()).load(uri).into(imageView);
                //skipBtn.setBackgroundColor(Color.parseColor("#2c8bff"));
                //skipBtn.setText("done");
            }
        };


         */
    }


    /*
    private static final int PICK_IMAGE = 100;

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            final Uri imageUri = data.getData();
            updateUserProfilePicture(imageUri);
        }
    }
    private void updateUserProfilePicture(final Uri uri) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        firebaseUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pictureListener.onProfilePictureUpdated();
                        }
                    }
                });
    }

     */
}