package com.example.kiosk;

public class SilogItem {
    private String name;
    private int price; // optional if you want

    public SilogItem() {}

    public SilogItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
}