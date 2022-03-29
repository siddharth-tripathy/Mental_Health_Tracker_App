package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PayAdmin extends AppCompatActivity implements PaymentResultListener {

    Button payAdminBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String nm, no;
    EditText payAdme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_admin);

        /*
        db.collection("DoctorUser").document(currentUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            nm = documentSnapshot.getString("Name");
                            no = documentSnapshot.getString("Number");
                        }
                    }
                });

        payAdme = findViewById(R.id.payAdme);
        String pad = String.valueOf(payAdme);
        pad = pad+"00";
        float p = Float.parseFloat(pad);

        payAdminBtn = findViewById(R.id.payAdminBtn);
        payAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_m8Mx6M6wvVB1qu");
                //checkout.setImage(R.drawable.nev_cart);
                JSONObject object = new JSONObject();
                try {
                    object.put("name", nm);
                    //object.put("description", "Test Payment");
                    object.put("theme.color", "#FF8C00");
                    object.put("currency", "INR");
                    object.put("amount", p);
                    object.put("prefill.contact", no);
                    checkout.open(PayAdmin.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         */

    }

    @Override
    public void onPaymentSuccess(String s) {
        String currentDateTimeString = DateFormat.getDateTimeInstance()
                .format(new Date());

        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("PaymentDate", currentDateTimeString);
        paymentData.put("PaymentAmount", "Rs. 1000");

        db.collection("DoctorUser").document(currentUser).collection("PayAdmin")
                .add(paymentData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        View v = findViewById(R.id.bkApp);
                        Snackbar snackbar = Snackbar.make(v, "PAYMENT SUCCESSFUL!!!", Snackbar.LENGTH_SHORT);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.primaryB));
                        snackbar.setTextColor(getResources().getColor(R.color.white));
                        snackbar.show();
                    }
                });
    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}