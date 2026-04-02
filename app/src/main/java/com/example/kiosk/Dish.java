package com.example.kiosk;

public class Dish {
    private String name;
    private int price;
    private int categoryId;

    public Dish(String name, int price, int categoryId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    // --- Optional: Setters if needed ---
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
