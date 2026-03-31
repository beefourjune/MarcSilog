package com.example.kiosk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private LinearLayout paymentContainer;
    private Button payNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentContainer = findViewById(R.id.paymentContainer);
        payNowBtn = findViewById(R.id.payNowBtn);

        // Get the cart items passed from CartActivity
        ArrayList<String> cartItems = getIntent().getStringArrayListExtra("cart_items");

        if (cartItems != null && !cartItems.isEmpty()) {
            Typeface manropeMedium = ResourcesCompat.getFont(this, R.font.manrope_medium);

            for (String item : cartItems) {
                // Split the string "MealName - ₱Price" into name and price
                String[] parts = item.split(" - ₱");
                String itemNameText = parts[0];
                String itemPriceText = "₱" + parts[1];

                // Horizontal layout for each item
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(16, 16, 16, 16);
                itemLayout.setGravity(Gravity.CENTER_VERTICAL);

                // Meal name (left)
                TextView itemName = new TextView(this);
                itemName.setText(itemNameText);
                itemName.setTypeface(manropeMedium);
                itemName.setTextColor(Color.BLACK);
                itemName.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2
                ));

                // Price (right)
                TextView itemPrice = new TextView(this);
                itemPrice.setText(itemPriceText);
                itemPrice.setTypeface(manropeMedium);
                itemPrice.setTextColor(Color.BLACK);
                itemPrice.setGravity(Gravity.END);
                itemPrice.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
                ));

                // Add name and price to horizontal layout
                itemLayout.addView(itemName);
                itemLayout.addView(itemPrice);

                // Add horizontal layout to payment container
                paymentContainer.addView(itemLayout);
            }
        } else {
            Toast.makeText(this, "No items to pay for!", Toast.LENGTH_SHORT).show();
        }

        // Handle Pay Now button click
        payNowBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();

            // --- Clear the cart ---
            MainMenu.cartList.clear();
            // If you have totals in MainMenu, reset them too
            // MainMenu.resetTotals();  // Uncomment if you implement resetTotals()

            // --- Return to MainMenu and clear back stack ---
            Intent intent = new Intent(PaymentActivity.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close PaymentActivity
        });
    }
}
