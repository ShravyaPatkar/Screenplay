package com.example.screenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.screenplay.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
  private  Button btn_register;
    private EditText InputEmail, InputPassword;

    private ProgressDialog loadingBar;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = (Button) findViewById(R.id.register_btn);
        InputEmail = (EditText) findViewById(R.id.register_email_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


        final String email = InputEmail.getText().toString().trim();
        final String password = InputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(register.this, "Please enter email..", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(register.this, "Please write your password..", Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length()<6)
        {
            Toast.makeText(register.this,"Password too short",Toast.LENGTH_LONG).show();
            return;
        }

                loadingBar.setTitle("Create account");
                loadingBar.setMessage("Please wait,while we are checking the credentials");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingBar.setTitle("Create account");
                        loadingBar.setMessage("Please wait,while we are checking the credentials");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        if (task.isSuccessful()) {

                            Users information = new Users(email,password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   // Toast.makeText(register.this, "Congratulations ,your account has been created. ", Toast.LENGTH_LONG).show();


                                   // startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                    sendEmailVerification();


                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(register.this,"Registration failed",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                        }

                        // ...
                    }
                });
            }
        });
    }
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(register.this,"Registration successful! Email Verification has been sent.Please verify your account to Sign In",Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(register.this,HomeActivity.class));

                    }
                    else
                    {
                        Toast.makeText(register.this,"Error:While sending Email verification",Toast.LENGTH_LONG);
                    }
                }
            });
        }
    }
}
