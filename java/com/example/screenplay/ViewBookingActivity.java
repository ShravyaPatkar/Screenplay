package com.example.screenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.screenplay.ViewHolder.reservedViewHolder;
import com.example.screenplay.model.reserved;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        recyclerView=findViewById(R.id.view_lists);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseRecyclerOptions<reserved>options=new FirebaseRecyclerOptions.Builder<reserved>()
                .setQuery(mDatabase.child("Booking Details"),reserved.class).build();
        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseRecyclerAdapter<reserved, reservedViewHolder> adapter=
                new FirebaseRecyclerAdapter<reserved, reservedViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull reservedViewHolder holder, int position, @NonNull reserved model) {

                    holder.txtmoviename.setText(model.getMovie_name());
                       holder.txtmovielanguage.setText(model.getMovie_language());
                        holder.txtlocation.setText(model.getLocation());
                        holder.txttheatres.setText(model.getSelected_theatre());
                        holder.txtshowdate.setText(model.getShow_date());
                        holder.txtshowtiming.setText(model.getShow_timing());
                        holder.txtseatnumber.setText(model.getSeat_number());
                        holder.txtbookedseats.setText(model.getNo_of_booked_seats());
                        holder.txttotalcost.setText(model.getTotal_cost());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]=new CharSequence[]
                                        {
                                           "Edit Booked Details",
                                           "Cancel booking"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(ViewBookingActivity.this);
                                builder.setTitle("Options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i==0)
                                        {
                                            Intent intent=new Intent(ViewBookingActivity.this,ShowTimingActivity.class);
                                            startActivity(intent);
                                        }
                                        if (i==1)
                                        {
                                            mDatabase.child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task)
                                                        {
                                                            if (task.isSuccessful())
                                                            {
                                                                Toast.makeText(ViewBookingActivity.this,"Money will be refunded to your account",Toast.LENGTH_SHORT).show();
                                                                Intent intent=new Intent(ViewBookingActivity.this,LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });
                                        }

                                    }
                                });
                                builder.show();

                            }
                        });
                    }


                    @NonNull
                    @Override
                    public reservedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_items_layout,parent,false);
                      reservedViewHolder holder = new reservedViewHolder(view);
                      return holder;

                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }
}
