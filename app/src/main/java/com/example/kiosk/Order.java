package com.example.kiosk;

import java.util.ArrayList;

public class Order {

    private String id;
    private String firebaseKey;

    private String status; // pending / preparing / ready

    private String orderType; // ✔ ADDED BACK (TAKE OUT / DINE IN)

    private long timestamp;
    private ArrayList<CartItem> items;
    private int total;
    private int totalPrice; // Support both names found in Firebase/Logs
    private boolean completed;
    private boolean inKitchen;
    private int stability;

    public Order() {
        this.status = "pending";
        this.completed = false;
        this.inKitchen = false;
        this.stability = 0;
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
        this.completed = false;
        this.inKitchen = false;
        this.stability = 0;

        // Calculate total
        this.total = 0;
        if (items != null) {
            for (CartItem item : items) {
                this.total += item.getPrice() * item.getQuantity();
            }
        }
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

    public int getTotal() {
        return total;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isInKitchen() {
        return inKitchen;
    }

    public int getStability() {
        return stability;
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

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setInKitchen(boolean inKitchen) {
        this.inKitchen = inKitchen;
    }

    public void setStability(int stability) {
        this.stability = stability;
    }
}