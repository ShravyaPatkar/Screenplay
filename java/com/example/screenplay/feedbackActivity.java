package com.example.screenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class feedbackActivity extends AppCompatActivity {
EditText emaildata,messagedata;
Button send,details;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        emaildata=findViewById(R.id.emaildata);
        messagedata=findViewById(R.id.messagedata);
        send=findViewById(R.id.send_btn);
        details=findViewById(R.id.details_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emaildata.getText().toString();
                String message=messagedata.getText().toString();
                details.setEnabled(true);

                mDatabase=FirebaseDatabase.getInstance().getReference();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Feedbacks");
                FirebaseDataMap obj =new FirebaseDataMap();
                final HashMap<String,Object> FeedbackMap=obj.firebaseMap();


                if(email.isEmpty()){
                    emaildata.setError("This is an required field");
                    send.setEnabled(false);
                }
                else
                {
                    emaildata.setError(null);
                    send.setEnabled(true);
                }
                FeedbackMap.put("Email",emaildata.getText().toString());

                if(message.isEmpty()){
                    messagedata.setError("This is an required field");
                    send.setEnabled(false);
                }
                else
                {
                    messagedata.setError(null);
                    send.setEnabled(true);
                }
                FeedbackMap.put("Feedback",messagedata.getText().toString());

                mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(FeedbackMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(feedbackActivity.this,"Your Feedback has been Sent..",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(feedbackActivity.this)
                                .setTitle("Sent Details:")
                                .setMessage("Email - " +email +"\n\nFeedback - " +message).show();
                    }
                });

            }
        });





    }
}
