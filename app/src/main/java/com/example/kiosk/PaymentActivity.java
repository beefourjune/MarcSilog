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

public class PaymentActivity extends AppCompatActivity {

    private LinearLayout paymentContainer;
    private Button payNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentContainer = findViewById(R.id.paymentContainer);
        payNowBtn = findViewById(R.id.payNowBtn);

        if (MainMenu.cartList != null && !MainMenu.cartList.isEmpty()) {

            Typeface manropeMedium =
                    ResourcesCompat.getFont(this, R.font.manrope_medium);

            int totalPrice = 0;

            for (CartItem item : MainMenu.cartList) {
                totalPrice += item.price * item.quantity;

                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(16, 16, 16, 16);
                itemLayout.setGravity(Gravity.CENTER_VERTICAL);

                TextView itemName = new TextView(this);
                itemName.setText(item.name + " x" + item.quantity);
                itemName.setTypeface(manropeMedium);
                itemName.setTextColor(Color.BLACK);
                itemName.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2
                ));

                TextView itemPrice = new TextView(this);
                itemPrice.setText("₱" + (item.price * item.quantity));
                itemPrice.setTypeface(manropeMedium);
                itemPrice.setTextColor(Color.BLACK);
                itemPrice.setGravity(Gravity.END);
                itemPrice.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                itemLayout.addView(itemName);
                itemLayout.addView(itemPrice);
                paymentContainer.addView(itemLayout);
            }

            TextView totalText = new TextView(this);
            totalText.setText("Total: ₱" + totalPrice);
            totalText.setTypeface(manropeMedium);
            totalText.setTextColor(Color.BLACK);
            totalText.setTextSize(18f);
            totalText.setGravity(Gravity.END);
            totalText.setPadding(16, 16, 16, 16);

            paymentContainer.addView(totalText);

            final int finalTotalPrice = totalPrice;

            payNowBtn.setOnClickListener(v ->
                    sendOrderToFirebase(finalTotalPrice)
            );

        } else {
            Toast.makeText(this, "No items to pay for!", Toast.LENGTH_SHORT).show();
            payNowBtn.setEnabled(false);
        }
    }

    // ================= FIREBASE ORDER SAVE (FIXED FINAL VERSION) =================

    private void sendOrderToFirebase(int totalPrice) {

        DatabaseReference rootRef =
                FirebaseDatabase.getInstance().getReference();

        DatabaseReference ordersRef = rootRef.child("orders");
        DatabaseReference counterRef = rootRef.child("counters").child("orderId");

        ArrayList<CartItem> itemsBackup =
                new ArrayList<>(MainMenu.cartList);

        counterRef.get().addOnSuccessListener(snapshot -> {

            int currentValue = 0;

            Integer dbValue = snapshot.getValue(Integer.class);
            if (dbValue != null) {
                currentValue = dbValue;
            }

            final int newNumber = currentValue + 1;
            final String displayId = String.format("%04d", newNumber);

            // 🔥 REAL FIREBASE KEY (THIS FIXES EVERYTHING)
            String firebaseKey = ordersRef.push().getKey();

            if (firebaseKey == null) {
                Toast.makeText(this,
                        "Failed to generate order key",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Order order = new Order(
                    displayId,
                    firebaseKey,
                    false,
                    false,
                    System.currentTimeMillis(),
                    itemsBackup
            );

            // 🔥 CRITICAL: SAVE BOTH IDS
            order.setId(displayId);
            order.setFirebaseKey(firebaseKey);

            ordersRef.child(firebaseKey).setValue(order)
                    .addOnSuccessListener(unused -> {

                        counterRef.setValue(newNumber);

                        MainMenu.cartList.clear();

                        Intent intent = new Intent(PaymentActivity.this, OrderReceivedActivity.class);

                        intent.putExtra("ORDER_ID", displayId);
                        intent.putExtra("FIREBASE_KEY", firebaseKey); // 🔥 IMPORTANT
                        intent.putExtra("TOTAL", totalPrice);

                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this,
                                    "Order failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show()
                    );
        });
    }
}