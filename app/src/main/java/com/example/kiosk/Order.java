package com.example.kiosk;

import java.util.ArrayList;

public class Order {

    private String id;          // 4-digit display ID
    private String firebaseKey; // real Firebase key

    private boolean completed;
    private boolean inKitchen;

    private long timestamp;
    private ArrayList<CartItem> items;

    public Order() {}

    public Order(String id,
                 String firebaseKey,
                 boolean completed,
                 boolean inKitchen,
                 long timestamp,
                 ArrayList<CartItem> items) {

        this.id = id;
        this.firebaseKey = firebaseKey;
        this.completed = completed;
        this.inKitchen = inKitchen;
        this.timestamp = timestamp;
        this.items = items;
    }

    public String getId() { return id; }
    public String getFirebaseKey() { return firebaseKey; }
    public boolean isCompleted() { return completed; }
    public boolean isInKitchen() { return inKitchen; }
    public long getTimestamp() { return timestamp; }
    public ArrayList<CartItem> getItems() { return items; }

    public void setId(String id) { this.id = id; }
    public void setFirebaseKey(String firebaseKey) { this.firebaseKey = firebaseKey; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setInKitchen(boolean inKitchen) { this.inKitchen = inKitchen; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setItems(ArrayList<CartItem> items) { this.items = items; }
}