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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DDoctorDetails extends AppCompatActivity {

    EditText DoctorDetailsName, DoctorDetailsEmail;
    Button DoctorDetailsSubmitBtn;
    LinearLayout biodata;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    FirebaseUser muser;

    String editMode, uName, uDOB, uGender, eGender, uBio, uLocation, uExp, uPatients, frm;
    TextView name, age, edit, dob, eDob, gender;
    EditText eName, eAge, eBio, eLocation, eExp, ePatients;
    CalendarView calendarView;
    RadioGroup radioGroup;
    RadioButton male, female, other;
    Button save, upload;
    ImageView profileImg;
    Uri url;

    //Firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String uNumber;
    private static final int PICK_PROFILE_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_doctor_details);

        //editMode = "false";

        Intent i = getIntent();
        editMode = i.getStringExtra("EditMode");
        uNumber = i.getStringExtra("Number");
        frm = i.getStringExtra("Frm");

        /*
        if (frm.equals("settings")){
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
                                    String p = document.getString("Profileimage");
                                    eName.setText(n);
                                    eDob.setText(db);
                                    gender.setText(g);
                                    Glide.with(DDoctorDetails.this)
                                            .load(p)
                                            .placeholder(R.drawable.profile)
                                            .into(profileImg);
                                } else {
                                    startActivity(new Intent(DDoctorDetails.this, TrackHistory.class));
                                }
                            }
                        }
                    });
        }

         */


        //TextView
        name  = (TextView)findViewById(R.id.name);
        edit = findViewById(R.id.edit);
        dob = findViewById(R.id.DoB);
        eDob = findViewById(R.id.EDoB);
        gender = findViewById(R.id.gender);

        //EditText
        eName = findViewById(R.id.editName);
        eBio = findViewById(R.id.bio);
        eExp = findViewById(R.id.exp);
        ePatients = findViewById(R.id.totalPatients);
        eLocation = findViewById(R.id.location);

        //Calendar
        calendarView = findViewById(R.id.calendarView);

        //RadioGroup
        radioGroup = findViewById(R.id.groupradio);

        //Radio Button
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);

        //Button
        save = findViewById(R.id.save);
        upload = findViewById(R.id.upload);

        //ImageView
        profileImg = findViewById(R.id.profileImg);

        biodata = findViewById(R.id.biodata);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DDoctorDetails.this, DDoctorDetails.class);
                i.putExtra("EditMode" , "true");
                startActivity(i);
            }
        });

        //Applying settings based on edit mode
        if (editMode.equals("true")) {
            name.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            dob.setVisibility(View.GONE);
            gender.setVisibility(View.GONE);
            biodata.setVisibility(View.GONE);

            eName.setVisibility(View.VISIBLE);
            eDob.setVisibility(View.VISIBLE);
            male.setVisibility(View.VISIBLE);
            female.setVisibility(View.VISIBLE);
            other.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            eBio.setVisibility(View.VISIBLE);
            eLocation.setVisibility(View.VISIBLE);
            ePatients.setVisibility(View.VISIBLE);
            eExp.setVisibility(View.VISIBLE);
        }


        if (editMode.equals("false")) {


            name.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            dob.setVisibility(View.VISIBLE);
            gender.setVisibility(View.VISIBLE);
            biodata.setVisibility(View.VISIBLE);



            eName.setVisibility(View.GONE);
            eDob.setVisibility(View.GONE);
            male.setVisibility(View.GONE);
            female.setVisibility(View.GONE);
            other.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            eBio.setVisibility(View.GONE);
            eLocation.setVisibility(View.GONE);
            ePatients.setVisibility(View.GONE);
            eExp.setVisibility(View.GONE);
            upload.setVisibility(View.GONE);






            db.collection("DoctorUser").document(currentUser)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();

                                if (document.exists()) {
                                    String n = document.getString("Name");
                                    String db = document.getString("DateOfBirth");
                                    String g = document.getString("Gender");
                                    String ProfileUrl = document.getString("Profileimage");
                                    String e = document.getString("Experience");
                                    String B = document.getString("Bio");
                                    String TP = document.getString("TotalPatients");

                                    name.setText(n);
                                    dob.setText(db);
                                    gender.setText(g);
                                    Glide.with(DDoctorDetails.this)
                                            .load(ProfileUrl)
                                            .into(profileImg);

                                }
                            }
                        }
                    });

        }

        //Date of Birth
        eDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });



        calendarView.setOnDateChangeListener(
                new CalendarView
                        .OnDateChangeListener() {
                    @Override

                    // In this Listener have one method
                    // and in this method we will
                    // get the value of DAYS, MONTH, YEARS
                    public void onSelectedDayChange(
                            @NonNull CalendarView view,
                            int year,
                            int month,
                            int dayOfMonth)
                    {

                        // Store the value of date with
                        // format in String type Variable
                        // Add 1 in month because month
                        // index is start with 0
                        String Date
                                = dayOfMonth + "-"
                                + (month + 1) + "-" + year;

                        // set this date in TextView for Display
                        eDob.setText(Date);
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
                                                 int checkedId)
                    {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton)group
                                .findViewById(checkedId);
                    }
                });

        //Saving data to database
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(DDoctorDetails.this,
                            "No answer has been selected",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                else {

                    RadioButton radioButton
                            = (RadioButton)radioGroup
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
                                        uBio = eBio.getText().toString();
                                        uExp = eExp.getText().toString();
                                        uLocation = eLocation.getText().toString();
                                        uPatients = ePatients.getText().toString();

                                        Map<String, Object> accountData = new HashMap<>();
                                        accountData.put("Name", uName);
                                        accountData.put("DateOfBirth", uDOB);
                                        accountData.put("Gender", uGender);
                                        accountData.put("UID",currentUser);
                                        accountData.put("Bio", uBio);
                                        accountData.put("Experience", uExp);
                                        accountData.put("TotalPatients", uPatients);
                                        accountData.put("Location", uLocation);
                                        accountData.put("Profileimage", profileImgUrl);
                                        accountData.put("Number", uNumber);
                                        if (frm.equals("signin")){
                                            accountData.put("Approval", "false");
                                        }
                                        else {
                                            accountData.put("Approval", "true");
                                        }

                                        db.collection("DoctorUser").document(currentUser)
                                                .set(accountData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("TAG", "DocumentSnapshot successfully written!");
                                                        startActivity(new Intent(DDoctorDetails.this, DocMainActivity.class));
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

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PROFILE_IMAGE);
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