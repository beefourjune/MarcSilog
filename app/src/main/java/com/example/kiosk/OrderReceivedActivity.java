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

public class OrderReceivedActivity extends AppCompatActivity {

    private LinearLayout receiptContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_received);

        receiptContainer = findViewById(R.id.receiptContainer);

        Typeface bold = ResourcesCompat.getFont(this, R.font.manrope_bold);
        Typeface medium = ResourcesCompat.getFont(this, R.font.manrope_medium);

        // 🔥 IMPORTANT: USE FIREBASE KEY (NOT DISPLAY ID)
        String firebaseKey = getIntent().getStringExtra("FIREBASE_KEY");
        String displayId = getIntent().getStringExtra("ORDER_ID");

        // ✅ ADDED: ORDER TYPE
        String orderType = getIntent().getStringExtra("order_type");

        if (orderType == null) {
            orderType = "TAKE OUT"; // fallback default
        }

        if (firebaseKey == null) {
            Toast.makeText(this, "Invalid Order", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DatabaseReference orderRef =
                FirebaseDatabase.getInstance()
                        .getReference("orders")
                        .child(firebaseKey);

        // ================= TITLE =================
        TextView title = new TextView(this);
        title.setText(R.string.receipt_title);
        title.setTextSize(22f);
        title.setTypeface(bold);
        title.setGravity(Gravity.CENTER);
        title.setPadding(20, 30, 20, 30);
        receiptContainer.addView(title);

        // ================= ORDER ID =================
        TextView idText = new TextView(this);
        idText.setText(getString(R.string.order_id_format, displayId));
        idText.setTextSize(18f);
        idText.setTypeface(bold);
        idText.setGravity(Gravity.CENTER);
        idText.setPadding(20, 10, 20, 10);
        receiptContainer.addView(idText);

        // ================= ORDER TYPE (NEW) =================
        TextView typeText = new TextView(this);
        typeText.setText(getString(R.string.order_type_format, orderType));
        typeText.setTextSize(18f);
        typeText.setTypeface(bold);
        typeText.setGravity(Gravity.CENTER);
        typeText.setPadding(20, 10, 20, 20);
        receiptContainer.addView(typeText);

        // ================= LOAD ORDER =================
        orderRef.get().addOnSuccessListener(snapshot -> {

            if (!snapshot.exists()) {
                Toast.makeText(this, "Order not found", Toast.LENGTH_LONG).show();
                return;
            }

            Order order = snapshot.getValue(Order.class);

            if (order == null || order.getItems() == null) {
                Toast.makeText(this, "No items found", Toast.LENGTH_LONG).show();
                return;
            }

            int total = 0;

            for (CartItem item : order.getItems()) {

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(20, 10, 20, 10);

                TextView name = new TextView(this);
                name.setText(getString(R.string.cart_item_quantity, item.name, item.quantity));
                name.setTypeface(medium);
                name.setTextColor(Color.BLACK);
                name.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                TextView price = new TextView(this);
                price.setText(getString(R.string.price_format, (item.price * item.quantity)));
                price.setTypeface(medium);
                price.setTextColor(Color.BLACK);

                row.addView(name);
                row.addView(price);

                receiptContainer.addView(row);

                total += item.price * item.quantity;
            }

            // ================= TOTAL =================
            TextView totalText = new TextView(this);
            totalText.setText(getString(R.string.total_price_format, total));
            totalText.setTextSize(20f);
            totalText.setTypeface(bold);
            totalText.setGravity(Gravity.END);
            totalText.setPadding(20, 30, 20, 30);

            receiptContainer.addView(totalText);

        }).addOnFailureListener(e ->
                Toast.makeText(this,
                        getString(R.string.order_failed, e.getMessage()),
                        Toast.LENGTH_LONG).show()
        );

        // ================= OK BUTTON =================
        Button okBtn = new Button(this);
        okBtn.setText(R.string.ok_button);

        okBtn.setOnClickListener(v -> {
            Intent intentNext = new Intent(this, MainMenu.class);
            intentNext.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentNext);
            finish();
        });

        receiptContainer.addView(okBtn);
    }
}