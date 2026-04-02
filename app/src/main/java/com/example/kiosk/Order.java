package com.example.kiosk;

public class Order {
    private String id;
    private String userId;
    private boolean isCompleted;
    private boolean isInKitchen;

    // Default constructor required for Firebase
    public Order() {}

    public Order(String id, String userId, boolean isCompleted, boolean isInKitchen) {
        this.id = id;
        this.userId = userId;
        this.isCompleted = isCompleted;
        this.isInKitchen = isInKitchen;
    }

    // --- Getter methods ---
    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isInKitchen() {
        return isInKitchen;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
}