package com.example.mentalhealthtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

//import com.google.android.gms.tasks.Task;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateAccount extends AppCompatActivity {
    String editMode, uName, uDOB, uGender, eGender, uNumber;
    TextView name, age, edit, dob, eDob, gender;
    EditText eName, eAge;
    RadioGroup radioGroup;
    RadioButton male, female, other;
    Button save, upload;
    ImageView profileImg;
    Uri url;

    //Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private static final int PICK_PROFILE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Checking mode
        editMode = "true";

        Intent i = getIntent();
        editMode = i.getStringExtra("EditMode");
        String frm = i.getStringExtra("From");
        if (frm.equals("signin") || frm.equals("dashboard"))
        {
            uNumber = i.getStringExtra("Number");
        }


        //TextView
        name = (TextView) findViewById(R.id.name);
        edit = findViewById(R.id.edit);
        dob = findViewById(R.id.DoB);
        eDob = findViewById(R.id.EDoB);
        gender = findViewById(R.id.gender);

        //EditText
        eName = findViewById(R.id.editName);

        //RadioGroup
        radioGroup = findViewById(R.id.groupradio);

        //Radio Button
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);

        //Button
        save = findViewById(R.id.save);
        upload = findViewById(R.id.upload);

        //Profile Image
        profileImg = findViewById(R.id.profileImg);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateAccount.this, CreateAccount.class);
                i.putExtra("EditMode", "true");
                i.putExtra("From", "edit");
                startActivity(i);
            }
        });

        //Applying settings based on edit mode
        if (editMode.equals(true)) {
            profileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_PROFILE_IMAGE);
                }
            });
        }

        if (editMode.equals("true")) {
            name.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            dob.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);


            eName.setVisibility(View.VISIBLE);
            eDob.setVisibility(View.VISIBLE);
            male.setVisibility(View.VISIBLE);
            female.setVisibility(View.VISIBLE);
            other.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            upload.setVisibility(View.VISIBLE);
        }

        if (editMode.equals("false")) {

            name.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            dob.setVisibility(View.VISIBLE);
            gender.setVisibility(View.VISIBLE);


            eName.setVisibility(View.GONE);
            eDob.setVisibility(View.GONE);
            male.setVisibility(View.GONE);
            female.setVisibility(View.GONE);
            other.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);

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
                                    String db = document.getString("DateOfBirth");
                                    String g = document.getString("Gender");
                                    String p = document.getString("ProfileImageUrl");
                                    name.setText(n);
                                    dob.setText(db);
                                    gender.setText(g);
                                    Glide.with(CreateAccount.this)
                                            .load(p)
                                            .placeholder(R.drawable.profile)
                                            .into(profileImg);
                                } else {
                                    startActivity(new Intent(CreateAccount.this, TrackHistory.class));
                                }
                            }
                        }
                    });
        }

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PROFILE_IMAGE);
            }
        });

        //Date of Birth
        eDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                handleDateButton();
            }
        });

        //Gender
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    // The flow will come here when
                    // any of the radio buttons in the radioGroup
                    // has been clicked

                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton) group
                                .findViewById(checkedId);
                    }
                });

        //Saving data to database
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(CreateAccount.this,
                            "No answer has been selected",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    RadioButton radioButton
                            = (RadioButton) radioGroup
                            .findViewById(selectedId);

                    // Now display the value of selected item
                    // by the Toast message

                    eGender = radioButton.getText().toString();

                    //Toast.makeText(CreateAccount.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                }

                profileImg.setDrawingCacheEnabled(true);
                profileImg.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) profileImg.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = storageReference.child("User").child("ProfileImage").child(currentUser + ".jpg").putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        storageReference.child("User").child("ProfileImage").child(currentUser + ".jpg").getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String profileImgUrl = task.getResult().toString();
                                        uName = eName.getText().toString();
                                        uDOB = eDob.getText().toString();
                                        uGender = eGender.toString();

                                        Map<String, Object> accountData = new HashMap<>();
                                        accountData.put("Name", uName);
                                        accountData.put("DateOfBirth", uDOB);
                                        accountData.put("Gender", uGender);
                                        accountData.put("Number", uNumber);
                                        accountData.put("ProfileImageUrl", profileImgUrl);

                                        db.collection("User").document(currentUser)
                                                .set(accountData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("TAG", "DocumentSnapshot successfully written!");
                                                        Toast.makeText(CreateAccount.this, "Saving Data", Toast.LENGTH_LONG).show();
                                                        if (frm.equals("signin")){
                                                            startActivity(new Intent(CreateAccount.this, Intro.class));
                                                        }
                                                        else {
                                                            startActivity(new Intent(CreateAccount.this, Dashboard.class));
                                                        }
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
                });
            }
        });
    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                eDob.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            url = data.getData();
            if (requestCode == PICK_PROFILE_IMAGE)
                profileImg.setImageURI(url);
        }
    }
}