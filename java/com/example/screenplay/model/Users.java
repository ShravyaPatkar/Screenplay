package com.example.screenplay.model;

public class Users {
    public String Email, password;

    public Users(){

    }

    public Users(String email, String password) {
        Email = email;
        this.password = password;
    }
}