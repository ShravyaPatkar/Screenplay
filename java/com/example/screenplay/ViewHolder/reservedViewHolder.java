package com.example.screenplay.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.screenplay.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class reservedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtmoviename,txtmovielanguage,txtlocation,txttheatres,txtshowdate,txtshowtiming,txtseatnumber,txtbookedseats,txttotalcost;
    //private ItemClickListener itemClickListener;

    public reservedViewHolder(@NonNull View itemView) {
        super(itemView);
        txtmoviename=itemView.findViewById(R.id.view_moviename);
        txtmovielanguage=itemView.findViewById(R.id.view_movielanguage);
        txtlocation=itemView.findViewById(R.id.view_location);
        txttheatres=itemView.findViewById(R.id.view_theatre);
        txtshowdate=itemView.findViewById(R.id.view_date);
        txtshowtiming=itemView.findViewById(R.id.view_timing);
        txtseatnumber=itemView.findViewById(R.id.view_seatnumber);
        txtbookedseats=itemView.findViewById(R.id.view_noofbookedseats);
        txttotalcost=itemView.findViewById(R.id.view_cost);


    }

    @Override
    public void onClick(View v) {

    }

}
