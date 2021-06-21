package com.example.screenplay.model;

import android.graphics.Movie;

import org.intellij.lang.annotations.Language;

public class reserved {
    private String movie_name,movie_language,location,selected_theatre,show_date,show_timing,seat_number,no_of_booked_seats,total_cost;

    public reserved() {
    }


    public reserved(String movie_name, String movie_language, String location, String selected_theatre, String show_date, String show_timing, String seat_number, String no_of_booked_seats, String total_cost) {
        this.movie_name = movie_name;
        this.movie_language = movie_language;
        this.location = location;
        this.selected_theatre = selected_theatre;
        this.show_date = show_date;
        this.show_timing = show_timing;
        this.seat_number = seat_number;
        this.no_of_booked_seats = no_of_booked_seats;
        this.total_cost = total_cost;

    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_language() {
        return movie_language;
    }

    public void setMovie_language(String movie_language) {
        this.movie_language = movie_language;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSelected_theatre() {
        return selected_theatre;
    }

    public void setSelected_theatre(String selected_theatre) {
        this.selected_theatre = selected_theatre;
    }

    public String getShow_date() {
        return show_date;
    }

    public void setShow_date(String show_date) {
        this.show_date = show_date;
    }

    public String getShow_timing() {
        return show_timing;
    }

    public void setShow_timing(String show_timing) {
        this.show_timing = show_timing;
    }

    public String getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(String seat_number) {
        this.seat_number = seat_number;
    }

    public String getNo_of_booked_seats() {
        return no_of_booked_seats;
    }

    public void setNo_of_booked_seats(String no_of_booked_seats) {
        this.no_of_booked_seats = no_of_booked_seats;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }
}
