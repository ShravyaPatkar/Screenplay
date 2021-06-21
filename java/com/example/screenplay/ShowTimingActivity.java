package com.example.screenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ShowTimingActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //date selection
    Button bdate;
    public EditText edate;
    private int day,month,year;

    //show timing
   public Button savetime,resettime;
  public   CheckBox c1,c2,c3,c4;

    public Spinner s1,s2;
   public Button bookseatbutton;
    private DatabaseReference mDatabase;
    Member member;
    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timing);

        member=new Member();
        bdate=(Button)findViewById(R.id.bdate);
        edate=(EditText)findViewById(R.id.edate);
       savetime=findViewById(R.id.savetime);
       bookseatbutton=findViewById(R.id.Bookseats);
        resettime=findViewById(R.id.resettime);
        c1=findViewById(R.id.sevenam);
        c2=findViewById(R.id.tenam);
        c3=findViewById(R.id.twopm);
        c4=findViewById(R.id.fivepm);
        s1 = (Spinner)findViewById(R.id.spinner1);
        s2 = (Spinner)findViewById(R.id.spinner2);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());




        String d1 ="7.00am - 10.00am";
        String d2 ="10.30am - 1.30pm";
        String d3 ="2.00pm - 5.00pm";
        String d4 ="5.30pm - 8.30pm";
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    i = (int)dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        s1.setOnItemSelectedListener(this);
        bookseatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowTimingActivity.this, BookSeatActivity.class));

                mDatabase=FirebaseDatabase.getInstance().getReference();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details");
                FirebaseDataMap obj =new FirebaseDataMap();
                final HashMap<String,Object>dataMap=obj.firebaseMap();

                dataMap.put("show_date",edate.getText().toString());
                dataMap.put("location",s1.getSelectedItem().toString());
                dataMap.put("selected_theatre",s2.getSelectedItem().toString());
               dataMap.put("show_timing",member.getShowTime());




                mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(dataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    //Toast.makeText(ShowTimingActivity.this,"Your movie ticket has been Booked",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }
        });


        savetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              FirebaseDataMap obj =new FirebaseDataMap();
                final HashMap<String,Object>dataMap=obj.firebaseMap();

                int cb_count=0;
                if(c1.isChecked()){
                    cb_count=cb_count+1;

                   member.setShowTime(d1);

                    mDatabase.updateChildren(dataMap);

                }else
                {
                    //
                }

                if(c2.isChecked()){
                    cb_count=cb_count+1;
                    member.setShowTime(d2);
                    mDatabase.updateChildren(dataMap);

                }else
                {
                    //
                }

                if(c3.isChecked()){
                    cb_count=cb_count+1;
                    member.setShowTime(d3);
                    mDatabase.updateChildren(dataMap);

                }else
                {
                    //
                }

                if(c4.isChecked()){
                    cb_count=cb_count+1;
                    member.setShowTime(d4);
                    //mDatabase.push().setValue(member);
                    mDatabase.updateChildren(dataMap);

                }else
                {
                    //
                }
            }
        });
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    c2.setChecked(false);
                    c3.setChecked(false);
                    c4.setChecked(false);
                }
            }
        });
        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    c1.setChecked(false);
                    c3.setChecked(false);
                    c4.setChecked(false);
                }
            }
        });
        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    c1.setChecked(false);
                    c2.setChecked(false);
                    c4.setChecked(false);
                }
            }
        });
        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    c2.setChecked(false);
                    c3.setChecked(false);
                    c1.setChecked(false);
                }
            }
        });
        resettime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1.isChecked())
                    c1.setChecked(false);
                if(c2.isChecked())
                    c2.setChecked(false);
                if(c3.isChecked())
                    c3.setChecked(false);
                if(c4.isChecked())
                    c4.setChecked(false);
            }
        });
        bdate.setOnClickListener(this);
    }



    //added for spinner
    public void onItemSelected(AdapterView<?>arg0,View arg1,int arg2,long arg3) {
        String sp1 = String.valueOf(s1.getSelectedItem());
        Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
        if (sp1.contentEquals("Manipal")) {

            List<String> list = new ArrayList<String>();
            list.add("Sri mastiyamma cine creation");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s2.setAdapter(dataAdapter);

        }

        if(sp1.contentEquals("Udupi")) {
            List<String> list = new ArrayList<String>();
            list.add("Alankar Theatre");
            list.add("Ashirvad Theatre");
            list.add("Kalpana Theatre");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            s2.setAdapter(dataAdapter2);

        }
    }


    public void onNothingSelected(AdapterView<?>arg0){

    }
    public void onClick(View v){
        if(v==bdate){
            final Calendar c= Calendar.getInstance();
            day=c.get(Calendar.DAY_OF_MONTH);
            month=c.get(Calendar.MONTH);
            year=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    edate.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);

                }

            }
            ,day,month,year);
            Date newDate=c.getTime();
            datePickerDialog.getDatePicker().setMinDate(newDate.getTime()-(newDate.getTime()%(24*40*40*1000)));
            datePickerDialog.show();
        }

    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
