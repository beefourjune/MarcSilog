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

    private Button backBtn, orderBtn;
    private LinearLayout cartContainer;
    private TextView emptyText, totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        backBtn = findViewById(R.id.backBtn);
        orderBtn = findViewById(R.id.orderBtn);
        cartContainer = findViewById(R.id.cartContainer);
        emptyText = findViewById(R.id.cartEmptyText);

        // Add a TextView for total price
        totalText = findViewById(R.id.cartTotalText);

        backBtn.setOnClickListener(v -> finish());

        updateCartUI();

        orderBtn.setOnClickListener(v -> {
            if (!MainMenu.cartList.isEmpty()) {
                Intent intent = new Intent(CartActivity.this, PaymentActivity.class);

                // Pass cart items (convert to Strings or keep as CartItem objects)
                ArrayList<String> items = new ArrayList<>();
                for (CartItem c : MainMenu.cartList) {
                    items.add(c.name + " - $" + c.price);
                }
                intent.putStringArrayListExtra("cart_items", items);
                startActivity(intent);

                // Clear cart
                MainMenu.cartList.clear();
                updateCartUI();
            } else {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartUI() {
        cartContainer.removeAllViews();

        if (MainMenu.cartList != null && !MainMenu.cartList.isEmpty()) {
            emptyText.setVisibility(TextView.GONE);
            cartContainer.setVisibility(LinearLayout.VISIBLE);
            orderBtn.setVisibility(Button.VISIBLE);
            totalText.setVisibility(TextView.VISIBLE);

            double totalPrice = 0.0;

            for (int i = 0; i < MainMenu.cartList.size(); i++) {
                CartItem item = MainMenu.cartList.get(i);
                totalPrice += item.price;

                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(16, 16, 16, 16);
                itemLayout.setGravity(Gravity.CENTER_VERTICAL);

                TextView itemName = new TextView(this);
                itemName.setText(item.name + " - $" + item.price);
                itemName.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                itemName.setTextSize(18f);

                Button deleteBtn = new Button(this);
                deleteBtn.setText("Delete");
                deleteBtn.setBackgroundTintList(ColorStateList.valueOf(
                        getResources().getColor(android.R.color.holo_red_light)
                ));
                deleteBtn.setTextColor(getResources().getColor(android.R.color.white));

                int finalI = i;
                deleteBtn.setOnClickListener(v -> {
                    CartItem removedItem = MainMenu.cartList.remove(finalI);
                    Toast.makeText(this, removedItem.name + " deleted", Toast.LENGTH_SHORT).show();
                    updateCartUI();
                });

                itemLayout.addView(itemName);
                itemLayout.addView(deleteBtn);
                cartContainer.addView(itemLayout);
            }

            totalText.setText("Total: $" + String.format("%.2f", totalPrice));

        } else {
            emptyText.setVisibility(TextView.VISIBLE);
            cartContainer.setVisibility(LinearLayout.GONE);
            orderBtn.setVisibility(Button.GONE);
            totalText.setVisibility(TextView.GONE);
        }
    }
}
