package com.example.kiosk;

public class Product {

    private String name;
    private int price;
    private int stock;
    private int imageResId; // Drawable resource ID for the product image

    // Default constructor required for Firebase
    public Product() {}

    // 4-parameter constructor (with image)
    public Product(String name, int price, int stock, int imageResId) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageResId = imageResId;
    }

    // 3-parameter constructor (optional, uses default imageResId = 0)
    public Product(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.imageResId = 0; // default placeholder
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public int getImageResId() { return imageResId; }

    // --- Setters ---
    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
}