package com.example.kiosk;

import android.os.Parcel;
import android.os.Parcelable;

// Model class for a cart item
public class CartItem implements Parcelable {

    public String name;
    public int price;

    public CartItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    protected CartItem(Parcel in) {
        name = in.readString();
        price = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeInt(price);
    }
}
