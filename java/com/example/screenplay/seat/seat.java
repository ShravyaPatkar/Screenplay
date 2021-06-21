package com.example.screenplay.seat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.screenplay.BookSeatActivity;
import com.example.screenplay.R;

public class seat extends TextView {

    public seat(Context context) {
        super(context);

    }

    public seat(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public seat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    private String seatNumber;

    private Boolean isSelected = false;
    public int resultCode;


    public Boolean getPayPalService() {
        return PayPalService;
    }

    public void setPayPalService(Boolean payPalService) {
        this.PayPalService= !this.PayPalService;
        if(getPayPalService()) {

            if (resultCode == Activity.RESULT_OK) {

                setBackgroundColor(0xc2c2a3);
            } else {
                setBackgroundColor(0xff99cc00);
            }
            PayPalService = payPalService;
        }

    }

    private Boolean PayPalService=false;
    private Boolean isWomen = false;
    private Double seatPrice = 100.00;
    private Double seatPriceVIP = 150.00;

    public Double getSeatPriceVIP() {
        return seatPriceVIP;
    }

    public void setSeatPriceVIP(Double seatPriceVIP) {
        this.seatPriceVIP = seatPriceVIP;
    }

    public Boolean getIsWomen() {
        return isWomen;
    }

    public void setIsWomen(Boolean isWomen) {
        this.isWomen = isWomen;
    }

    public Double getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(Double seatPrice) {
        this.seatPrice = seatPrice;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public Boolean setSelected() {

        this.isSelected = !this.isSelected;

            if (getIsSelected()) {
                setBackgroundColor(Color.GRAY);
            }
            else {
                setBackgroundColor(0xff33b5e5);
            }
            return isSelected;

    }

   public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;

        setText(seatNumber);
    }

}
