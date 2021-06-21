package com.example.screenplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.screenplay.seat.seat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.HashMap;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class BookSeatActivity extends AppCompatActivity {
    private final int NUMBER_OF_SEATS_LEFT_Golden = 5;
    private final int NUMBER_OF_ROWS_LEFT_Golden = 4;
    private final int NUMBER_OF_SEATS_LEFT_Silver = 5;
    private final int NUMBER_OF_ROWS_LEFT_Silver = 4;
    private final int NUMBER_OF_SEATS_LEFT_VIP = 8;
    private final int NUMBER_OF_ROWS_LEFT_VIP = 1;

    private Double totalCost = 0.0;
    private int totalSeats = 0;
    private TextView totalPrice;
    private TextView totalBookedSeats;
    private TextView ClickPayment;
    public static final Boolean paymentstate= FALSE;
    public int PAYPAL_REQ_CODE=10;
    private static PayPalConfiguration payPalConfig =new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Paypalclintidconfigclass.PAYPAL_CLIENT_ID);


    private LinearLayout.LayoutParams seatParams;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_seat);
        // Fetch Required Layouts
        RelativeLayout seatLayoutGolden = (RelativeLayout) findViewById(R.id.golden);
        RelativeLayout seatLayoutSilver = (RelativeLayout) findViewById(R.id.silver);
        RelativeLayout seatLayoutVIP = (RelativeLayout) findViewById(R.id.VIP);
        totalPrice = (TextView) findViewById(R.id.total_cost);
        totalBookedSeats = (TextView) findViewById(R.id.total_seats);
        ClickPayment = (TextView)findViewById(R.id.clickforpayment) ;
       mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        final seat leftRowSeat = (seat) LayoutInflater.from(this).inflate(R.layout.activity_seat, null);
       Intent intent = new Intent(this,PayPalService.class);
       intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfig);
       startService(intent);



        ClickPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PaypalPaymentsMethod();

                mDatabase=FirebaseDatabase.getInstance().getReference();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details");
                FirebaseDataMap obj =new FirebaseDataMap();
                final HashMap<String,Object>dataMap=obj.firebaseMap();
                dataMap.put("no_of_booked_seats",totalBookedSeats.getText().toString());
                dataMap.put("total_Cost",totalPrice.getText().toString());

                mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(dataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                   // Toast.makeText(BookSeatActivity.this,"Your movie ticket has been Booked",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        // Layout Param for Seats
        seatParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        seatParams.weight = 1;
        seatParams.leftMargin = 5;
        addgoldenSeats(NUMBER_OF_ROWS_LEFT_Golden, NUMBER_OF_SEATS_LEFT_Golden,seatLayoutGolden);
        addsilverSeats(NUMBER_OF_ROWS_LEFT_Silver, NUMBER_OF_SEATS_LEFT_Silver,seatLayoutSilver);
        addVIPSeats(NUMBER_OF_ROWS_LEFT_VIP, NUMBER_OF_SEATS_LEFT_VIP,seatLayoutVIP);

    }

    private void PaypalPaymentsMethod() {
        final seat leftRowSeat = (seat) LayoutInflater.from(this).inflate(R.layout.activity_seat, null);
        PayPalPayment  payment =new PayPalPayment(new BigDecimal(String.valueOf(totalPrice.getText().toString())),
                "USD", "Payments",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfig);
       intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent,PAYPAL_REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQ_CODE)
        {
            final seat leftRowSeat = (seat) LayoutInflater.from(this).inflate(R.layout.activity_seat, null);
            if (resultCode == Activity.RESULT_OK)
            {

                Toast.makeText(this,"Payment made Successfully",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Payment is unsuccessful",Toast.LENGTH_LONG).show();

            }
            leftRowSeat.setPayPalService(true);

            mDatabase=FirebaseDatabase.getInstance().getReference();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            FirebaseDataMap obj =new FirebaseDataMap();
            final HashMap<String,Object>dataMap=obj.firebaseMap();


        }
    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }



    private void addgoldenSeats(int numberOfRowsLeft, float numberOfSeatsInRow, ViewGroup bookseat) {
        int previousRow = 0;
        for (int leftRowCount = 0; leftRowCount < numberOfRowsLeft; leftRowCount++) {
            // Adding Linear layout as row
            LinearLayout LeftRow = new LinearLayout(getApplicationContext());
            LeftRow.setGravity(Gravity.CENTER);
            LeftRow.setId( 100 + leftRowCount);
            // Equi distance seats
            LeftRow.setWeightSum(numberOfSeatsInRow);
            // if it is first row add row to window
            // else add row below window row (BELOW)
            if (previousRow != leftRowCount) {
                RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                newParams.addRule(RelativeLayout.BELOW, 100+ previousRow);
                newParams.setMargins(10, 10, 10, 10);
                LeftRow.setLayoutParams(newParams);
            } else {
                RelativeLayout.LayoutParams leftRowParam = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                leftRowParam.setMargins(10, 10, 10, 10);
                LeftRow.setLayoutParams(leftRowParam);
            }
            // Add Seats in row
            for (int rowCount = 0; rowCount <= numberOfSeatsInRow; rowCount++) {
                // Left Upper
                final seat leftRowSeat = (seat) LayoutInflater.from(this).inflate(R.layout.activity_seat, null);
                leftRowSeat.setLayoutParams(seatParams);

                if(leftRowCount==0)
                {
                    leftRowSeat.setSeatNumber("GA" + rowCount);

                }
                else
                    if(leftRowCount==1)
                    {
                        leftRowSeat.setSeatNumber("GB" + rowCount);

                    }
                    else
                        if (leftRowCount==2)
                        {
                            leftRowSeat.setSeatNumber("GC" + rowCount);

                        }
                        else
                            if(leftRowCount==3)
                            {
                                leftRowSeat.setSeatNumber("GD" + rowCount);

                            }

                            leftRowSeat.setGravity(Gravity.CENTER);



                leftRowSeat.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        leftRowSeat.setEnabled(false);
                        leftRowSeat.setBackgroundColor(0xc2c2a3);
                        updateCost(leftRowSeat);
                        FirebaseDataMap obj =new FirebaseDataMap();
                        final HashMap<String,Object>dataMap=obj.firebaseMap();
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Toast.makeText(getApplicationContext(),""+ leftRowSeat.getSeatNumber(),Toast.LENGTH_LONG).show();
                        dataMap.put("seat_number",leftRowSeat.getSeatNumber());

                        mDatabase.updateChildren(dataMap);







                    }
                });
                LeftRow.addView(leftRowSeat);
            }
            // add row to bus layout
            bookseat.addView(LeftRow);
            // update row counter
            previousRow = leftRowCount;
        }
    }
    private void addsilverSeats(int numberOfRowsLeft, float numberOfSeatsInRow, ViewGroup bookseat) {
        int previousRow = 0;
        for (int leftRowCount = 0; leftRowCount < numberOfRowsLeft; leftRowCount++) {
            // Adding Linear layout as row
            LinearLayout LeftRow = new LinearLayout(getApplicationContext());
            LeftRow.setGravity(Gravity.CENTER);
            LeftRow.setId( 100 + leftRowCount);
            // Equi distance seats
            LeftRow.setWeightSum(numberOfSeatsInRow);
            // if it is first row add row to window
            // else add row below window row (BELOW)
            if (previousRow != leftRowCount) {
                RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                newParams.addRule(RelativeLayout.BELOW, 100+ previousRow);
                newParams.setMargins(10, 10, 10, 10);
                LeftRow.setLayoutParams(newParams);
            } else {
                RelativeLayout.LayoutParams leftRowParam = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                leftRowParam.setMargins(10, 10, 10, 10);
                LeftRow.setLayoutParams(leftRowParam);
            }
            // Add Seats in row
            for (int rowCount = 0; rowCount <= numberOfSeatsInRow; rowCount++) {

                // Left Upper
                final seat leftRowSeat = (seat) LayoutInflater.from(this).inflate(R.layout.activity_seat, null);
                leftRowSeat.setLayoutParams(seatParams);
                FirebaseDataMap obj =new FirebaseDataMap();

                final HashMap<String,Object>dataMap=obj.firebaseMap();
                leftRowSeat.setLayoutParams(seatParams);
                if(leftRowCount==0)
                {
                    leftRowSeat.setSeatNumber("SA" + rowCount);


                }
                else
                if(leftRowCount==1)
                {
                    leftRowSeat.setSeatNumber("SB" + rowCount);

                }
                else
                if (leftRowCount==2)
                {
                    leftRowSeat.setSeatNumber("SC" + rowCount);

                }
                else
                if(leftRowCount==3)
                {
                    leftRowSeat.setSeatNumber("SD" + rowCount);

                }


                leftRowSeat.setGravity(Gravity.CENTER);

                leftRowSeat.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        leftRowSeat.setEnabled(false);
                        leftRowSeat.setBackgroundColor(0xc2c2a3);
                        updateCost(leftRowSeat);

                        FirebaseDataMap obj =new FirebaseDataMap();
                        final HashMap<String,Object>dataMap=obj.firebaseMap();
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Toast.makeText(getApplicationContext(), "Silver seat " + leftRowSeat.getSeatNumber(),Toast.LENGTH_LONG).show();
                        dataMap.put("seat_number",leftRowSeat.getSeatNumber());
                        mDatabase.updateChildren(dataMap);







                    }
                });
                LeftRow.addView(leftRowSeat);
            }
            // add row to bus layout
            bookseat.addView(LeftRow);
            // update row counter
            previousRow = leftRowCount;
        }
    }

    private void addVIPSeats(int numberOfRowsLeft, float numberOfSeatsInRow, ViewGroup bookseat) {
        int previousRow = 0;
        for (int leftRowCount = 0; leftRowCount < numberOfRowsLeft; leftRowCount++) {
            // Adding Linear layout as row
            LinearLayout LeftRow = new LinearLayout(getApplicationContext());
            LeftRow.setGravity(Gravity.CENTER);
            LeftRow.setId( 100 + leftRowCount);
            // Equi distance seats
            LeftRow.setWeightSum(numberOfSeatsInRow);
            // if it is first row add row to window
            // else add row below window row (BELOW)
            if (previousRow != leftRowCount) {
                RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                newParams.addRule(RelativeLayout.BELOW, 100+ previousRow);
                newParams.setMargins(10, 10, 10, 10);
                LeftRow.setLayoutParams(newParams);
            } else {
                RelativeLayout.LayoutParams leftRowParam = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                leftRowParam.setMargins(10, 10, 10, 10);
                LeftRow.setLayoutParams(leftRowParam);
            }
            // Add Seats in row
            for (int rowCount = 0; rowCount <= numberOfSeatsInRow; rowCount++) {

                // Left Upper
                final seat leftRowSeat = (seat) LayoutInflater.from(this).inflate(R.layout.activity_seat, null);
                leftRowSeat.setLayoutParams(seatParams);
                FirebaseDataMap obj =new FirebaseDataMap();
                final HashMap<String,Object>dataMap=obj.firebaseMap();
                leftRowSeat.setLayoutParams(seatParams);
                leftRowSeat.setEnabled(true);
                if(leftRowCount==0)
                {
                    leftRowSeat.setSeatNumber("VA" + rowCount);

                }
                else
                if(leftRowCount==1)
                {
                    leftRowSeat.setSeatNumber("VB" + rowCount);

                }
                else
                if (leftRowCount==2)
                {
                    leftRowSeat.setSeatNumber("VC" + rowCount);

                }
                else
                if(leftRowCount==3)
                {
                    leftRowSeat.setSeatNumber("VD" + rowCount);

                }


                leftRowSeat.setGravity(Gravity.CENTER);

                leftRowSeat.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        leftRowSeat.setEnabled(false);
                        leftRowSeat.setBackgroundColor(Color.GRAY);
                        updateCostVIP(leftRowSeat);

                        FirebaseDataMap obj =new FirebaseDataMap();
                        final HashMap<String,Object>dataMap=obj.firebaseMap();
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Toast.makeText(getApplicationContext(), "VIP seat " + leftRowSeat.getSeatNumber(),Toast.LENGTH_LONG).show();
                        dataMap.put("seat_number",leftRowSeat.getSeatNumber());
                        mDatabase.updateChildren(dataMap);







                    }
                });
                LeftRow.addView(leftRowSeat);
            }
            // add row to bus layout
            bookseat.addView(LeftRow);
            // update row counter
            previousRow = leftRowCount;
        }
    }





    private void updateCost(final seat ud_LeftWindowSeat) {

        if (ud_LeftWindowSeat.setSelected()) {

            totalCost += ud_LeftWindowSeat.getSeatPrice();
            ++totalSeats;

        } else {
            totalCost -= ud_LeftWindowSeat.getSeatPrice();
            --totalSeats;
        }

        totalPrice.setText("" + totalCost);
        totalBookedSeats.setText("" + totalSeats);
    }

    private void updateCostVIP(final seat ud_LeftWindowSeat) {

        if (ud_LeftWindowSeat.setSelected()) {

            totalCost += ud_LeftWindowSeat.getSeatPriceVIP();
            ++totalSeats;

        } else {
            totalCost -= ud_LeftWindowSeat.getSeatPriceVIP();
            --totalSeats;
        }

        totalPrice.setText("" + totalCost);
        totalBookedSeats.setText("" + totalSeats);
    }
}
