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
        title.setText("RECEIPT");
        title.setTextColor(Color.BLACK);
        title.setTextSize(22f);
        title.setTypeface(bold);
        title.setGravity(Gravity.CENTER);
        title.setPadding(20, 30, 20, 30);
        receiptContainer.addView(title);

        // ================= ORDER ID (DISPLAY ONLY) =================
        TextView idText = new TextView(this);
        idText.setText("ORDER ID: " + displayId);
        idText.setTextSize(18f);
        idText.setTextColor(Color.BLACK);
        idText.setTypeface(bold);
        idText.setGravity(Gravity.CENTER);
        idText.setPadding(20, 10, 20, 20);
        receiptContainer.addView(idText);

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
                name.setText(item.name + " x" + item.quantity);
                name.setTypeface(medium);
                name.setTextColor(Color.BLACK);
                name.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                TextView price = new TextView(this);
                price.setText("₱" + (item.price * item.quantity));
                price.setTypeface(medium);
                price.setTextColor(Color.BLACK);

                row.addView(name);
                row.addView(price);

                receiptContainer.addView(row);

                total += item.price * item.quantity;
            }

            // ================= TOTAL =================
            TextView totalText = new TextView(this);
            totalText.setText("TOTAL: ₱" + total);
            totalText.setTextSize(20f);
            totalText.setTypeface(bold);
            totalText.setTextColor(Color.BLACK);
            totalText.setGravity(Gravity.END);
            totalText.setPadding(20, 30, 20, 30);

            receiptContainer.addView(totalText);

        }).addOnFailureListener(e ->
                Toast.makeText(this,
                        "Failed to load order: " + e.getMessage(),
                        Toast.LENGTH_LONG).show()
        );

        // ================= OK BUTTON =================
        Button okBtn = new Button(this);
        okBtn.setText("OK");
        okBtn.setTextSize(18f);
        okBtn.setTypeface(bold);
        okBtn.setBackgroundColor(Color.parseColor("#6A0DAD"));
        okBtn.setTextColor(Color.WHITE);


        okBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        receiptContainer.addView(okBtn);
    }
}