package com.example.kiosk;

import java.util.ArrayList;

public class Order {

    private String id;
    private String firebaseKey;

    private String status; // pending / preparing / ready

    private String orderType; // ✔ ADDED BACK (TAKE OUT / DINE IN)

    private long timestamp;
    private ArrayList<CartItem> items;

    public Order() {
        this.status = "pending";
    }

    public Order(String id,
                 String firebaseKey,
                 String status,
                 long timestamp,
                 ArrayList<CartItem> items) {

        this.id = id;
        this.firebaseKey = firebaseKey;
        this.status = status;
        this.timestamp = timestamp;
        this.items = items;
    }

    // ================= GETTERS =================

    public String getId() {
        return id;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderType() {
        return orderType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    // ================= SETTERS =================

    public void setId(String id) {
        this.id = id;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setItems(ArrayList<CartItem> items) {
        this.items = items;
    }
}