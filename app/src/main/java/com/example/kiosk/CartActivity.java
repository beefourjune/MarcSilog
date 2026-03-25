package com.example.kiosk;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TextView cartText = findViewById(R.id.cartText);

        ArrayList<String> cart = getIntent().getStringArrayListExtra("cart");

        if (cart != null && !cart.isEmpty()) {
            cartText.setText(cart.toString());
        } else {
            cartText.setText("Cart is empty");
        }
    }
}