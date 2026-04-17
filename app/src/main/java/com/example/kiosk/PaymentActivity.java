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

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    // ================= ORDER TYPE =================
    private String orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        LinearLayout paymentContainer = findViewById(R.id.paymentContainer);
        Button payNowBtn = findViewById(R.id.payNowBtn);
        MaterialButton backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(PaymentActivity.this, CartActivity.class));
            finish();
        });

        // ================= ORDER TYPE FIX (CLEAN) =================
        Intent intent = getIntent();
        String receivedType = intent.getStringExtra("order_type");

        if (receivedType != null && !receivedType.isEmpty()) {
            orderType = receivedType;
            MainMenu.orderType = receivedType;
        } else {
            orderType = MainMenu.orderType;
        }

        if (orderType == null || orderType.isEmpty()) {
            orderType = "TAKE OUT";
            MainMenu.orderType = "TAKE OUT";
        }

        // ================= CART CHECK =================
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
                itemName.setText(getString(R.string.cart_item_quantity, item.name, item.quantity));
                itemName.setTypeface(manropeMedium);
                itemName.setTextColor(Color.BLACK);
                itemName.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2
                ));

                TextView itemPrice = new TextView(this);
                itemPrice.setText(getString(R.string.price_format, item.price * item.quantity));
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
            totalText.setText(getString(R.string.total_price_format, totalPrice));
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
            Toast.makeText(this, R.string.no_items_to_pay, Toast.LENGTH_SHORT).show();
            payNowBtn.setEnabled(false);
        }
    }

    // ================= FIREBASE ORDER SAVE =================
    private void sendOrderToFirebase(int totalPrice) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ordersRef = rootRef.child("orders");
        DatabaseReference counterRef = rootRef.child("counters").child("orderId");

        ArrayList<CartItem> itemsBackup = new ArrayList<>(MainMenu.cartList);

        counterRef.get().addOnSuccessListener(snapshot -> {

            int currentValue = 0;

            Integer dbValue = snapshot.getValue(Integer.class);
            if (dbValue != null) {
                currentValue = dbValue;
            }

            final int newNumber = currentValue + 1;
            final String displayId = String.format(Locale.getDefault(), "%04d", newNumber);

            String firebaseKey = ordersRef.push().getKey();

            if (firebaseKey == null) {
                Toast.makeText(this,
                        R.string.failed_generate_key,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Order order = new Order(
                    displayId,
                    firebaseKey,
                    "pending",
                    System.currentTimeMillis(),
                    itemsBackup
            );

            order.setId(displayId);
            order.setFirebaseKey(firebaseKey);

            // KEEP ORDER TYPE
            order.setOrderType(orderType);

            ordersRef.child(firebaseKey).setValue(order)
                    .addOnSuccessListener(unused -> {

                        counterRef.setValue(newNumber);

                        MainMenu.cartList.clear();

                        // ❌ FIX: DO NOT RESET ORDER TYPE HERE ANYMORE

                        Intent next = new Intent(PaymentActivity.this, OrderReceivedActivity.class);

                        next.putExtra("ORDER_ID", displayId);
                        next.putExtra("FIREBASE_KEY", firebaseKey);
                        next.putExtra("TOTAL", totalPrice);
                        next.putExtra("order_type", orderType);

                        startActivity(next);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this,
                                    getString(R.string.order_failed, e.getMessage()),
                                    Toast.LENGTH_SHORT).show()
                    );
        });
    }
}