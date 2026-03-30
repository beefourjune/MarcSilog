package com.example.kiosk;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private Button backBtn;
    private Button orderBtn;
    private LinearLayout cartContainer;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        backBtn = findViewById(R.id.backBtn);
        orderBtn = findViewById(R.id.orderBtn);
        cartContainer = findViewById(R.id.cartContainer);
        emptyText = findViewById(R.id.cartEmptyText);

        // Back button closes this activity
        backBtn.setOnClickListener(v -> finish());

        // Show cart items
        if (MainMenu.cartList != null && !MainMenu.cartList.isEmpty()) {
            emptyText.setVisibility(TextView.GONE);
            cartContainer.setVisibility(LinearLayout.VISIBLE);
            orderBtn.setVisibility(Button.VISIBLE);

            // Clear previous views
            cartContainer.removeAllViews();

            // Add each cart item with Delete button
            for (String item : MainMenu.cartList) {
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(16, 16, 16, 16);
                itemLayout.setGravity(Gravity.CENTER_VERTICAL);

                // Item name
                TextView itemName = new TextView(this);
                itemName.setText(item);
                itemName.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                itemName.setTextSize(18f);

                // Delete button
                Button deleteBtn = new Button(this);
                deleteBtn.setText("Delete");
                deleteBtn.setBackgroundTintList(ColorStateList.valueOf(
                        getResources().getColor(android.R.color.holo_red_light)
                ));
                deleteBtn.setTextColor(getResources().getColor(android.R.color.white));
                deleteBtn.setOnClickListener(v -> {
                    // Remove from cart list
                    MainMenu.cartList.remove(item);
                    // Remove the view
                    cartContainer.removeView(itemLayout);
                    Toast.makeText(this, item + " deleted", Toast.LENGTH_SHORT).show();

                    // If cart is empty after deletion
                    if (MainMenu.cartList.isEmpty()) {
                        emptyText.setVisibility(TextView.VISIBLE);
                        cartContainer.setVisibility(LinearLayout.GONE);
                        orderBtn.setVisibility(Button.GONE);
                    }
                });

                // Add views to item layout
                itemLayout.addView(itemName);
                itemLayout.addView(deleteBtn);

                // Add item layout to container
                cartContainer.addView(itemLayout);
            }

        } else {
            emptyText.setVisibility(TextView.VISIBLE);
            cartContainer.setVisibility(LinearLayout.GONE);
            orderBtn.setVisibility(Button.GONE);
        }

        // Handle order button click -> go to PaymentActivity and pass cart items
        orderBtn.setOnClickListener(v -> {
            if (!MainMenu.cartList.isEmpty()) {
                // Create Intent and pass cart items
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                intent.putStringArrayListExtra("cart_items", new ArrayList<>(MainMenu.cartList));
                startActivity(intent);

                // Clear the cart
                MainMenu.cartList.clear();

                // Update UI
                cartContainer.removeAllViews();
                cartContainer.setVisibility(LinearLayout.GONE);
                emptyText.setVisibility(TextView.VISIBLE);
                orderBtn.setVisibility(Button.GONE);
            } else {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}