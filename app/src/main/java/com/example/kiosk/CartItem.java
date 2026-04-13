package com.example.kiosk;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

// Model class for a cart item
public class CartItem implements Parcelable {

    public String name;
    public int price;
    public int imageResId;
    public String description;

    public int quantity;

    public String key;

    // Required empty constructor for Firebase
    public CartItem() {
    }

    public CartItem(String name, int price, int imageResId, String description) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.description = description;
        this.quantity = 1;
    }

    public CartItem(String name, int price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 1;
        this.description = "";
    }

    public CartItem(String name, int price, int imageResId, int quantity, String description) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = quantity;
        this.description = description;
    }

    // Parcelable constructor
    protected CartItem(Parcel in) {
        name = in.readString();
        price = in.readInt();
        imageResId = in.readInt();
        quantity = in.readInt();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeInt(imageResId);
        dest.writeInt(quantity);
        dest.writeString(description);
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

    // Optional helper for Firebase upload
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("price", price);
        result.put("imageResId", imageResId);
        result.put("quantity", quantity);
        result.put("description", description);
        return result;
    }

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
        this.quantity = quantity;
    }
}