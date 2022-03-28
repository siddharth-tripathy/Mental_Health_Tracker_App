package com.example.mentalhealthtracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ManageOtp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;

    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;

    // string for storing our verification ID
    private String verificationId;

    private String phone;

    private Button verifyOTPBtn;

    Spinner spinner_join_as;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ProgressDialog progressDialog;
    String joinAs, newUser="false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_otp);

        //Spinner
        spinner_join_as = findViewById(R.id.spinner_join_as);
        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this, R.array.spinner_join_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_join_as.setAdapter(adapterC);
        spinner_join_as.setOnItemSelectedListener(this);

        //ProgressBar progressBar = new ProgressDialog(View.getContext());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            progressDialog = new ProgressDialog(this);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setTitle("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            db.collection("User").document(currentUser)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                             if (documentSnapshot.exists()){
                                startActivity(new Intent(ManageOtp.this, Dashboard.class));
                            }
                            else{
                                 db.collection("List").document(currentUser)
                                         .get()
                                         .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                             @Override
                                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                 if (task.isSuccessful()){
                                                     DocumentSnapshot documentSnapshot = task.getResult();
                                                     String join = documentSnapshot.getString("JoinAs");
                                                     String n = documentSnapshot.getString("Number");
                                                     Log.d("TAG", "The user is"+joinAs);

                                                     Intent i;
                                                     if (join.equals("User")){
                                                         //Log.d("TAG", "The user is here is "+joinAs);
                                                         i = new Intent(ManageOtp.this, CreateAccount.class);
                                                     }
                                                     else{
                                                         i = new Intent(ManageOtp.this, DDoctorDetails.class);
                                                     }
                                                     i.putExtra("EditMode","true");
                                                     i.putExtra("From", "signin");
                                                     i.putExtra("Number", n);
                                                     startActivity(i);
                                                     finish();
                                                 }
                                             }
                                         });
                            }
                            progressDialog.dismiss();
                            finish();
                        }
                    });
        }

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        // initializing variables for button and Edittext.
        edtPhone = findViewById(R.id.idEdtPhoneNumber);
        edtOTP = findViewById(R.id.idEdtOtp);
        // buttons for generating OTP and verifying OTP
        verifyOTPBtn = findViewById(R.id.idBtnVerify);
        Button generateOTPBtn = findViewById(R.id.idBtnGetOtp);

        // setting onclick listener for generate OTP button.
        generateOTPBtn.setOnClickListener(v -> {
            // below line is for checking weather the user
            // has entered his mobile number or not.
            phone = "+91" + edtPhone.getText().toString();

            if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                // when mobile number text field is empty
                // displaying a toast message.
                Toast.makeText(ManageOtp.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            } else {
                // if the text field is not empty we are calling our
                // send OTP method for getting OTP from Firebase.
                if (joinAs.equals("Doctor")){
                    db.collection("DoctorUser").whereEqualTo("Number", phone)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.isEmpty()){
                                        db.collection("User").whereEqualTo("Number",phone)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        QuerySnapshot querySnapshot = task.getResult();
                                                        if (querySnapshot.isEmpty()){
                                                            sendVerificationCode(phone);
                                                            Toast.makeText(ManageOtp.this, "Processing Request for new User", Toast.LENGTH_LONG).show();
                                                            newUser = "true";
                                                        }
                                                        else {
                                                            Toast.makeText(ManageOtp.this, "Enter Correct Credentials", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                        sendVerificationCode(phone);
                                        Toast.makeText(ManageOtp.this, "Processing Your Request Doctor...", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                if (joinAs.equals("Patient")){
                    db.collection("User").whereEqualTo("Number", phone)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    QuerySnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.isEmpty()){
                                        db.collection("DoctorUser").whereEqualTo("Number",phone)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        QuerySnapshot querySnapshot = task.getResult();
                                                        if (querySnapshot.isEmpty()){
                                                            newUser = "true";
                                                            sendVerificationCode(phone);
                                                            Toast.makeText(ManageOtp.this, "Processing Request for new User", Toast.LENGTH_LONG).show();
                                                        }
                                                        else {
                                                            Toast.makeText(ManageOtp.this, "Enter Correct Credentials"+newUser, Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                    else {
                                        sendVerificationCode(phone);
                                        Toast.makeText(ManageOtp.this, "Processing Your Request...", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                }

            }
        });




        // initializing on click listener
        // for verify otp button
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(ManageOtp.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    ManageOtp.this.verifyCode(edtOTP.getText().toString());
                    Toast.makeText(ManageOtp.this, "Verifying OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // if the code is correct and the task is successful
                        // we are sending our user to new activity.

                        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String val= "User";
                        if (joinAs.equals("Doctor")){
                            val = "DoctorUser";
                        }
                        else if (joinAs.equals("Patient")){
                            val = "User";
                        }

                        Map<String, Object> signInDet = new HashMap<>();
                        signInDet.put("JoinAs", val);
                        signInDet.put("Number", phone);

                        db.collection("List").document(currentUser)
                                .set(signInDet)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.d("TAG", "DocumentSnapshot successfully written!");
                                        }
                                    }
                                });

                        db.collection(val).document(currentUser)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if (documentSnapshot.exists()) {
                                            if (joinAs.equals("Doctor")){
                                                startActivity(new Intent(ManageOtp.this, DocMainActivity.class));
                                            }
                                            else if (joinAs.equals("Patient")){
                                                startActivity(new Intent(ManageOtp.this, Dashboard.class));
                                            }
                                        } else {
                                            if (joinAs.equals("Patient")){
                                                //String n = edtPhone.getText().toString();
                                                Toast.makeText(getApplicationContext(),"Patient", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(ManageOtp.this, CreateAccount.class);
                                                i.putExtra("EditMode","true");
                                                i.putExtra("From", "signin");
                                                i.putExtra("Number", phone);
                                                startActivity(i);
                                            }
                                            else if(joinAs.equals("Doctor")){
                                                //TO BE COMPLETED
                                                Toast.makeText(getApplicationContext(),"Doctor", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(ManageOtp.this, DDoctorDetails.class);
                                                i.putExtra("EditMode","true");
                                                i.putExtra("From", "signin");
                                                i.putExtra("Number", phone);
                                                startActivity(i);
                                            }
                                        }
                                        //progressDialog.dismiss();
                                        finish();
                                    }
                                });
                    } else {
                        // if the code is not correct then we are
                        // displaying an error message to the user.
                        Toast.makeText(ManageOtp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            spinner_join_as.setEnabled(false);
            edtPhone.setEnabled(false);
            verificationId = s;
            edtOTP.setVisibility(View.VISIBLE);
            verifyOTPBtn.setVisibility(View.VISIBLE);
            //progressDialog.dismiss();
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(ManageOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        joinAs = adapterView.getItemAtPosition(i).toString();
        Log.d("TAG", joinAs);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}