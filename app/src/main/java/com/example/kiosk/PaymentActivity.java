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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private LinearLayout paymentContainer;
    private Button payNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentContainer = findViewById(R.id.paymentContainer);
        payNowBtn = findViewById(R.id.payNowBtn);

        // Get cart items from MainMenu.cartList
        if (MainMenu.cartList != null && !MainMenu.cartList.isEmpty()) {
            Typeface manropeMedium = ResourcesCompat.getFont(this, R.font.manrope_medium);

            int totalPrice = 0;

            for (CartItem item : MainMenu.cartList) {
                totalPrice += item.price * item.quantity;

                // Horizontal layout for each item
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(16, 16, 16, 16);
                itemLayout.setGravity(Gravity.CENTER_VERTICAL);

                // Meal name (left)
                TextView itemName = new TextView(this);
                itemName.setText(item.name);
                itemName.setTypeface(manropeMedium);
                itemName.setTextColor(Color.BLACK);
                itemName.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2
                ));

                // Price (right)
                TextView itemPrice = new TextView(this);
                itemPrice.setText("₱" + item.price);
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

            // Display total at the bottom
            TextView totalText = new TextView(this);
            totalText.setText("Total: ₱" + totalPrice);
            totalText.setTypeface(manropeMedium);
            totalText.setTextColor(Color.BLACK);
            totalText.setTextSize(18f);
            totalText.setGravity(Gravity.END);
            totalText.setPadding(16, 16, 16, 16);
            paymentContainer.addView(totalText);

            // Handle Pay Now button click
            int finalTotalPrice = totalPrice;
            payNowBtn.setOnClickListener(v -> sendOrderToFirebase(finalTotalPrice));

        } else {
            Toast.makeText(this, "No items to pay for!", Toast.LENGTH_SHORT).show();
            payNowBtn.setEnabled(false);
        }
    }

    private void sendOrderToFirebase(int totalPrice) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = ordersRef.push().getKey();

        ArrayList<Map<String, Object>> itemsList = new ArrayList<>();
        for (CartItem item : MainMenu.cartList) {
            itemsList.add(item.toMap());
        }

        HashMap<String, Object> order = new HashMap<>();
        order.put("items", itemsList);
        order.put("total", totalPrice);

        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(PaymentActivity.this, "Payment successful! Order sent to admin.", Toast.LENGTH_LONG).show();

                    // Clear cart
                    MainMenu.cartList.clear();

                    // Return to MainMenu and clear back stack
                    Intent intent = new Intent(PaymentActivity.this, MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(PaymentActivity.this, "Failed to send order: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}