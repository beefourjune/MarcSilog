package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartListener {

    private MaterialButton backBtn;
    private RecyclerView recyclerView;
    private Button orderBtn;
    private TextView emptyText, totalText;

    private CartAdapter cartAdapter;
    private List<CartItem> cartList;

    // ================= ORDER TYPE =================
    private String orderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // ================= UI INIT =================
        backBtn = findViewById(R.id.backBtn);
        orderBtn = findViewById(R.id.orderBtn);
        emptyText = findViewById(R.id.cartEmptyText);
        totalText = findViewById(R.id.cartTotalText);
        recyclerView = findViewById(R.id.recyclerViewCart);

        // ================= USE MAIN CART =================
        cartList = MainMenu.cartList;

        // ================= FIX: SINGLE SOURCE OF TRUTH =================
        orderType = MainMenu.orderType;

        if (orderType == null || orderType.trim().isEmpty()) {
            orderType = "TAKE OUT";
        }

        // ================= RECYCLER =================
        cartAdapter = new CartAdapter(this, cartList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        // ================= BACK =================
        backBtn.setOnClickListener(v -> finish());

        // ================= ORDER BUTTON =================
        orderBtn.setOnClickListener(v -> {

            if (cartList != null && !cartList.isEmpty()) {

                Intent paymentIntent = new Intent(CartActivity.this, PaymentActivity.class);

                // STILL PASS IT (backup only)
                paymentIntent.putExtra("order_type", orderType);

                startActivity(paymentIntent);

            } else {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            }
        });

        updateCartUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartAdapter.notifyDataSetChanged();
        updateCartUI();
    }

    // ================= UI UPDATE =================
    private void updateCartUI() {

        if (cartList != null && !cartList.isEmpty()) {

            emptyText.setVisibility(TextView.GONE);
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            orderBtn.setVisibility(Button.VISIBLE);
            totalText.setVisibility(TextView.VISIBLE);

            int totalPrice = 0;

            for (CartItem item : cartList) {
                totalPrice += item.getPrice() * item.getQuantity();
            }

            totalText.setText("Total: ₱" + totalPrice);

        } else {

            emptyText.setVisibility(TextView.VISIBLE);
            recyclerView.setVisibility(RecyclerView.GONE);
            orderBtn.setVisibility(Button.GONE);
            totalText.setVisibility(TextView.GONE);
        }
    }

    // ================= REFRESH =================
    public void refreshCart() {
        cartAdapter.notifyDataSetChanged();
        updateCartUI();
    }

    // ================= CALLBACK =================
    @Override
    public void onQuantityChanged() {
        cartAdapter.notifyDataSetChanged();
        updateCartUI();
    }
}