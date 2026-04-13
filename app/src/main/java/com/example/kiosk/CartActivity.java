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

    @Override
    protected void onResume() {
        super.onResume();
        cartAdapter.notifyDataSetChanged();
        updateCartUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        backBtn = findViewById(R.id.backBtn);
        orderBtn = findViewById(R.id.orderBtn);
        emptyText = findViewById(R.id.cartEmptyText);
        totalText = findViewById(R.id.cartTotalText);
        recyclerView = findViewById(R.id.recyclerViewCart);

        cartList = MainMenu.cartList; // assuming cartList is static in MainMenu

        // RecyclerView setup
        cartAdapter = new CartAdapter(this, cartList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        backBtn.setOnClickListener(v -> finish());

        orderBtn.setOnClickListener(v -> {
            if (!cartList.isEmpty()) {
                startActivity(new Intent(CartActivity.this, AddOnsActivity.class));
            } else {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            }
        });

        updateCartUI();
    }

    // Called whenever the cart changes
    private void updateCartUI() {
        if (cartList != null && !cartList.isEmpty()) {
            emptyText.setVisibility(TextView.GONE);
            recyclerView.setVisibility(RecyclerView.VISIBLE);
            orderBtn.setVisibility(Button.VISIBLE);
            totalText.setVisibility(TextView.VISIBLE);

            // Calculate total price
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

    public void refreshCart() {
        cartAdapter.notifyDataSetChanged();
        updateCartUI();
    }
    // This is called from CartAdapter whenever quantity changes or item removed
    @Override
    public void onQuantityChanged() {
        cartAdapter.notifyDataSetChanged(); // refresh adapter
        updateCartUI(); // update total & visibility
    }
}