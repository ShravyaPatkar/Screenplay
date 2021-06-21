package com.example.screenplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ratingsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button button_sbm;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        ratingBar=findViewById(R.id.ratingBar_id);
        result=findViewById(R.id.text_id);
        button_sbm=findViewById(R.id.subrating);

        result.setText("Rating:"+ratingBar.getRating());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                result.setText("Rating: "+rating);

            }
        });

        button_sbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ratingsActivity.this,String.valueOf(ratingBar.getRating()),Toast.LENGTH_SHORT).show();

            }
        });

    }
}
