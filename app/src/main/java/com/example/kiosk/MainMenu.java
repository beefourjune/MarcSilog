package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    // --- Buttons ---
    MaterialButton backBtn, orderNowBtn, cartBtn;
    MaterialButton addBaconBtn, addTapBtn, addTosilogBtn;
    ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    // --- Cart UI ---
    TextView cartInfo;
    View cartPanel;
    MaterialButton viewCartBtn;

    // --- Cart data ---
    public static ArrayList<CartItem> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        // --- CART UI ---
        cartPanel = findViewById(R.id.cartPanel);
        cartInfo = findViewById(R.id.cartInfo);
        viewCartBtn = findViewById(R.id.viewCartBtn);

        // Hide cart panel initially if empty
        if (cartPanel != null) cartPanel.setVisibility(View.GONE);

        // Click on the floating panel opens cart
        if (cartPanel != null) {
            cartPanel.setOnClickListener(v -> openCart());
        }

        // Click on "View Cart" button
        if (viewCartBtn != null) {
            viewCartBtn.setOnClickListener(v -> openCart());
        }

        // --- BACK BUTTON ---
        backBtn = findViewById(R.id.backtostartbtn);
        if (backBtn != null) backBtn.setOnClickListener(v -> finish());

        // --- CART BUTTON ---
        cartBtn = findViewById(R.id.cartBtn);
        if (cartBtn != null) cartBtn.setOnClickListener(v -> openCart());

        // --- CATEGORY BUTTONS ---
        silogBtn = findViewById(R.id.silogBtn);
        pastilBtn = findViewById(R.id.pastilBtn);
        shawarmaBtn = findViewById(R.id.shawarmaBtn);
        sizzlingBtn = findViewById(R.id.sizzlingBtn);
        ssdBtn = findViewById(R.id.ssdBtn);
        drinkBtn = findViewById(R.id.drinkBtn);

        if (silogBtn != null) silogBtn.setOnClickListener(v -> showToast("Silog clicked"));
        if (pastilBtn != null) pastilBtn.setOnClickListener(v -> showToast("Pastil clicked"));
        if (shawarmaBtn != null) shawarmaBtn.setOnClickListener(v -> showToast("Shawarma clicked"));
        if (sizzlingBtn != null) sizzlingBtn.setOnClickListener(v -> showToast("Sizzling clicked"));
        if (ssdBtn != null) ssdBtn.setOnClickListener(v -> showToast("SSD clicked"));
        if (drinkBtn != null) drinkBtn.setOnClickListener(v -> showToast("Drinks clicked"));

        // --- ADD TO CART BUTTONS ---
        addBaconBtn = findViewById(R.id.addBaconBtn);
        addTapBtn = findViewById(R.id.addTapBtn);
        addTosilogBtn = findViewById(R.id.addTosilogBtn);

        if (addBaconBtn != null) addBaconBtn.setOnClickListener(v -> addItemToCart("BaconSilog", 45));
        if (addTapBtn != null) addTapBtn.setOnClickListener(v -> addItemToCart("TapSilog", 45));
        if (addTosilogBtn != null) addTosilogBtn.setOnClickListener(v -> addItemToCart("ToSilog", 39));

        // Optional other buttons
        setupAddButton(R.id.addBurgerBtn, "Burger Steak", 25);
        setupAddButton(R.id.addShawarmaBtn, "Shawarma", 25);
        setupAddButton(R.id.addSiomaiBtn, "Siomai Rice", 35);
        setupAddButton(R.id.addBigSiomaiBtn, "Big Siomai", 50);
        setupAddButton(R.id.addDumplingBtn, "Dumpling Rice", 45);
        setupAddButton(R.id.addSiopaoBtn, "Siopao", 30);

        // --- ORDER BUTTON ---
        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) orderNowBtn.setOnClickListener(v -> showToast("Proceeding to Order..."));

        // --- Refresh cart panel if items already exist ---
        refreshCartInfo();
    }

    // --- Setup generic add button ---
    private void setupAddButton(int id, String itemName, int price) {
        MaterialButton btn = findViewById(id);
        if (btn != null) btn.setOnClickListener(v -> addItemToCart(itemName, price));
    }

    // --- Add item to cart ---
    private void addItemToCart(String itemName, int price) {
        cartList.add(new CartItem(itemName, price));

        // Show cart panel if hidden
        if (cartPanel != null && cartPanel.getVisibility() == View.GONE) {
            cartPanel.setVisibility(View.VISIBLE);
        }

        // Refresh cart info
        refreshCartInfo();

        Toast.makeText(this, itemName + " added to cart", Toast.LENGTH_SHORT).show();
    }

    // --- Refresh floating cart info ---
    private void refreshCartInfo() {
        int totalItems = 0;
        int totalPrice = 0;

        for (CartItem item : cartList) {
            totalItems++;
            totalPrice += item.price;
        }

        if (cartInfo != null) {
            if (totalItems > 0) {
                cartInfo.setText(totalItems + " items - ₱" + totalPrice);
                if (cartPanel != null) cartPanel.setVisibility(View.VISIBLE);
            } else {
                cartInfo.setText("Cart is empty");
                if (cartPanel != null) cartPanel.setVisibility(View.GONE);
            }
        }
    }

    // --- Open CartActivity ---
    private void openCart() {
        Intent intent = new Intent(MainMenu.this, CartActivity.class);
        intent.putParcelableArrayListExtra("cart_items", new ArrayList<>(cartList));
        startActivity(intent);
    }

    // --- Utility ---
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}