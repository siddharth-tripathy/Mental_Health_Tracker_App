package com.example.mentalhealthtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Analysis extends AppCompatActivity {

    String questions_depression[] = {
            "Little interest or pleasure in doing things",
            "Feeling down, depressed or hopeless",
            "Trouble falling/staying asleep, sleeping too much",
            "Feeling tired or having little energy",
            "Poor appetite or overeating",
            "Feeling bad about yourself or that you are a failure or have let yourself or your family down",
            "Trouble concentrating on things, such as reading the newspaper or watching television.",
            "Moving or speaking so slowly that other people could have noticed. Or the opposite; being so fidgety or restless that you have been moving around a lot more than usual.",
            "Thoughts that you would be better off dead or of hurting yourself in some way."
    };

    String questions_anger[] = {
            "Little interest or pleasure in doing things",
            "Feeling down, depressed or hopeless",
            "Trouble falling/staying asleep, sleeping too much",
            "Feeling tired or having little energy",
            "Poor appetite or overeating",
            "Feeling bad about yourself or that you are a failure or have let yourself or your family down",
            "Trouble concentrating on things, such as reading the newspaper or watching television.",
            "Moving or speaking so slowly that other people could have noticed. Or the opposite; being so fidgety or restless that you have been moving around a lot more than usual.",
            "Thoughts that you would be better off dead or of hurting yourself in some way."
    };

    String questions_anxiety[] = {
            "Little interest or pleasure in doing things",
            "Feeling down, depressed or hopeless",
            "Trouble falling/staying asleep, sleeping too much",
            "Feeling tired or having little energy",
            "Poor appetite or overeating",
            "Feeling bad about yourself or that you are a failure or have let yourself or your family down",
            "Trouble concentrating on things, such as reading the newspaper or watching television.",
            "Moving or speaking so slowly that other people could have noticed. Or the opposite; being so fidgety or restless that you have been moving around a lot more than usual.",
            "Thoughts that you would be better off dead or of hurting yourself in some way."
    };

    String questions_sleep[] = {
            "Little interest or pleasure in doing things",
            "Feeling down, depressed or hopeless",
            "Trouble falling/staying asleep, sleeping too much",
            "Feeling tired or having little energy",
            "Poor appetite or overeating",
            "Feeling bad about yourself or that you are a failure or have let yourself or your family down",
            "Trouble concentrating on things, such as reading the newspaper or watching television.",
            "Moving or speaking so slowly that other people could have noticed. Or the opposite; being so fidgety or restless that you have been moving around a lot more than usual.",
            "Thoughts that you would be better off dead or of hurting yourself in some way."
    };


    int flag = 0;
    int result = 0;
    String resultDB;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        RadioButton r1 = findViewById(R.id.opt1);
        RadioButton r2 = findViewById(R.id.opt2);
        RadioButton r3 = findViewById(R.id.opt3);
        RadioButton r4 = findViewById(R.id.opt4);
        TextView ques = findViewById(R.id.ques);
        Button nxtQ = findViewById(R.id.nxtQ);
        LinearLayout resultDisplay = findViewById(R.id.resultDisplay);
        TextView resD = findViewById(R.id.resD);

        ques.setText(questions_depression[flag]);
        flag++;

        nxtQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (r1.isChecked())
                {
                    result = result+1;
                    r1.setChecked(false);
                }
                else if (r2.isChecked())
                {
                    result = result+2;
                    r2.setChecked(false);
                }
                else if (r3.isChecked())
                {
                    result = result+3;
                    r3.setChecked(false);
                }
                else if (r4.isChecked())
                {
                    result = result+4;
                    r4.setChecked(false);
                }

                ques.setText(questions_depression[flag]);
                flag++;

                if (flag==8)
                {
                    String  currentDateTimeString = DateFormat.getDateTimeInstance()
                            .format(new Date());
                    String s = "Depression/" + currentDateTimeString;
                    resultDB = String.valueOf(result);
                    Map<String, Object> resultData = new HashMap<>();
                    resultData.put(s, resultDB);

                    final String TAG = "MyActivity";

                    Task<Void> result_data = db.collection(currentUser).document("Analysis Report").collection("Depression").document(currentDateTimeString)
                            .set(resultData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                    resultDisplay.setVisibility(View.VISIBLE);
                    resD.setText(resultDB);
                }
            }
        });
    }
}