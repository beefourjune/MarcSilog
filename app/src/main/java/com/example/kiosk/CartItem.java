package com.example.kiosk;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class CartItem implements Parcelable {

    public String name;
    public int price;
    public int imageResId;
    public int quantity;

    // ================= EMPTY CONSTRUCTOR (FIREBASE) =================
    public CartItem() {
        this.name = "";
        this.price = 0;
        this.imageResId = 0;
        this.quantity = 1;
    }

    // ================= MAIN MERGED CONSTRUCTOR =================
    public CartItem(String name, int price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 1;
    }

    // ================= FULL CONSTRUCTOR =================
    public CartItem(String name, int price, int imageResId, int quantity) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = quantity;
    }

    // ================= KEY CONSTRUCTOR (FOR NORMALIZED CART HANDLING) =================
    public CartItem(String key, String name, int price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 1;
    }

    // ================= INCREASE QUANTITY =================
    public void increaseQuantity() {
        this.quantity++;
    }

    // ================= DECREASE QUANTITY =================
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    // ================= TOTAL PRICE =================
    public int getTotalPrice() {
        return price * quantity;
    }

    // ================= FIREBASE MAP =================
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("price", price);
        result.put("imageResId", imageResId);
        result.put("quantity", quantity);
        return result;
    }

    // ================= GETTERS / SETTERS =================
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(1, quantity);
    }

    // ================= PARCELABLE =================
    protected CartItem(Parcel in) {
        name = in.readString();
        price = in.readInt();
        imageResId = in.readInt();
        quantity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeInt(imageResId);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}