package com.example.kiosk;

public class Admin {
    public String username;
    public String password;

    public Admin() {} // Required for Firebase

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}