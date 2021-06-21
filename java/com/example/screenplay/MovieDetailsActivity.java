package com.example.screenplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.screenplay.ViewHolder.CategoryViewHolder;
import com.example.screenplay.film.CategoryItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MovieDetailsActivity extends AppCompatActivity {
//editeddb
public TextView description,language;
ImageView image;

private String moviedescription,movielanguage,movieimage;
Button BookTicketButton;
    private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        description=findViewById(R.id.description_details);
        language=findViewById(R.id.language_details);
        image=findViewById(R.id.Image_details);
        moviedescription= getIntent().getStringExtra("description");
        movielanguage=getIntent().getStringExtra("language");
        movieimage=getIntent().getStringExtra("image");
        description.setText(moviedescription);
        language.setText(movielanguage);
        Picasso.get().load(movieimage).into(image);
       // mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details").child(FirebaseAuth.getInstance().getCurrentUser().getUid());;



        BookTicketButton = (Button) findViewById(R.id.BookTickets);

        BookTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MovieDetailsActivity.this, ShowTimingActivity.class));
                mDatabase=FirebaseDatabase.getInstance().getReference();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Booking Details");
                FirebaseDataMap obj =new FirebaseDataMap();
                final HashMap<String,Object> dataMap=obj.firebaseMap();
                dataMap.put("movie_name",description.getText().toString());
                dataMap.put("movie_language",language.getText().toString());
                mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(dataMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                   // Toast.makeText(MovieDetailsActivity.this,"Your movie ticket has been Booked",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });


    }


}
