package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            for (String item : cartItems) {
                TextView tv = new TextView(this);
                tv.setText("• " + item);
                tv.setTextSize(18f);
                tv.setPadding(0, 8, 0, 8);
                paymentContainer.addView(tv);
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
