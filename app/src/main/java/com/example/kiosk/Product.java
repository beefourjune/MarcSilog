package com.example.kiosk;

public class Product {

    private String name;
    private int price;
    private int stock;
    private int imageResId;

    public String description;
    private String category; // ✅ NEW

    // Default constructor (REQUIRED for Firebase)
    public Product() {}

    // Full constructor (recommended)
    public Product(String name, int price, int stock, int imageResId, String category, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageResId = imageResId;
        this.category = category;
        this.description = description;
    }

    // Old constructor (kept for compatibility)
    public Product(String name, int price, int stock, int imageResId) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageResId = imageResId;
        this.category = "Others"; // default
    }

    // Optional constructor
    public Product(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageResId = 0;
        this.category = "Others";
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; } // ✅ NEW

    public String getDescription() {
        return description;
    }

    // --- Setters ---
    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public void setCategory(String category) { this.category = category; } //
    public void setDescription(String description) { this.description = description; }
    // ✅ NEW
}