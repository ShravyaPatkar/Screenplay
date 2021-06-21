package com.example.screenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

//edited for bookdb
     public EditText InputEmail, InputPassword;
     Button LoginButton;
     Button joinNowButton;
     CheckBox remember;

    public FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;
    private TextView forgotPassword;
    private DatabaseReference mDatabase;

   // private  TextView AdminLink,NotAdminLink;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());;



        joinNowButton = (Button) findViewById(R.id.main_join_now_btn);
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, register.class));

            }
        });

        InputEmail = (EditText) findViewById(R.id.login_email_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        remember = (CheckBox) findViewById(R.id.remember_me_chkb);
        LoginButton = (Button) findViewById(R.id.login_btn);





        forgotPassword = (TextView)findViewById(R.id.forgot_password_link);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,PasswordActivity.class));
            }
        });

        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        /*rememeber me*/
        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
       String checkbox = preferences.getString("remember","");

       if(checkbox.equals("true"))
       {
           startActivity(new Intent(HomeActivity.this, LoginActivity.class));
       }
       else if(checkbox.equals("false"))
       {
        Toast.makeText(this,"Please Sign In",Toast.LENGTH_SHORT).show();
       }



        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString( "remember","true");
                    editor.apply();
                    Toast.makeText(HomeActivity.this,"checked",Toast.LENGTH_SHORT).show();

                }else if(!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString( "remember","false");
                    editor.apply();
                    Toast.makeText(HomeActivity.this,"Unchecked",Toast.LENGTH_SHORT).show();

                }
            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = InputEmail.getText().toString();
                String password = InputPassword.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(HomeActivity.this,"Please enter email..",Toast.LENGTH_LONG).show();

                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(HomeActivity.this,"Please write your password..",Toast.LENGTH_LONG).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if(task.isSuccessful()){
                                    loadingBar.setTitle("Signing in...");
                                    loadingBar.setMessage("Please wait we are checking the credentials");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    loadingBar.setCanceledOnTouchOutside(false);
                                  /*  loadingBar.show();*/
                                    VerifyEmailFirst();

                                    mDatabase= FirebaseDatabase.getInstance().getReference();
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details");
                                    FirebaseDataMap obj =new FirebaseDataMap();
                                    final HashMap<String,Object> dataMap=obj.firebaseMap();
                                    dataMap.put("account",InputEmail.getText().toString());
                                    //String userId = user.getEmail();

                                    mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(dataMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(HomeActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(HomeActivity.this,"Login failed or user not available !",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });



    }
    private void VerifyEmailFirst()
    {
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailverify = firebaseUser.isEmailVerified();
        if(emailverify)
        {
            finish();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
        }
        else
        {
            Toast.makeText(this,"Please varify your Email",Toast.LENGTH_LONG).show();
            firebaseAuth.signOut();
        }
    }

}