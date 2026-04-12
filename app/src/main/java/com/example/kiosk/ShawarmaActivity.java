package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShawarmaActivity extends AppCompatActivity {

    private List<Product> shawarmaList;
    private RecyclerView recyclerView;
    private ShawarmaAdapter adapter;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    // Floating cart
    private MaterialCardView floatingCartPanel;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shawarma);

        setupCategoryButtons();

        // ================= BACK =================
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        // ================= FLOATING CART =================
        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        goToCartBtn.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );

        // ================= LIST =================
        shawarmaList = new ArrayList<>();
        addShawarmaProducts();

        recyclerView = findViewById(R.id.shawarmaRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ShawarmaAdapter(
                this,
                shawarmaList,
                this::updateFloatingCart
        );

        recyclerView.setAdapter(adapter);

        updateFloatingCart();
    }

    // ================= LOAD PRODUCTS =================
    private void addShawarmaProducts() {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("categories/shawarma");

        Object[][] items = {
                {"shawarma_rice", "Shawarma Rice", 120, R.drawable.shawarmarice},
                {"shawarma_double_rice", "Shawarma Double Rice", 140, R.drawable.shawarmarice},
                {"shawarma_burger", "Shawarma Burger", 90, R.drawable.shawarmaburger},
                {"shawarma_burger_fries", "Shawarma Burger w/ Fries", 110, R.drawable.shawarma_burger_fries},
                {"shawarma_pita", "Shawarma Pita", 80, R.drawable.shawarmapita}
        };

        int defaultStock = 10;

        for (Object[] item : items) {

            String key = (String) item[0];
            String name = (String) item[1];
            int price = (int) item[2];
            int imageResId = (int) item[3];

            Product product = new Product(name, price, defaultStock, imageResId);

            ref.child(key).setValue(product);
            shawarmaList.add(product);
        }
    }

    // ================= FLOATING CART (FIXED LIKE SILOG) =================
    private void updateFloatingCart() {

        int totalItems = 0;
        int totalPrice = 0;

        for (CartItem item : MainMenu.cartList) {
            totalItems += item.quantity;
            totalPrice += item.price * item.quantity;
        }

        if (totalItems > 0) {
            floatingCartPanel.setVisibility(android.view.View.VISIBLE);
            cartItemCount.setText(totalItems + " items - ₱" + totalPrice);
        } else {
            floatingCartPanel.setVisibility(android.view.View.GONE);
        }
    }

    // ================= CATEGORY BUTTONS =================
    private void setupCategoryButtons() {

        silogBtn = findViewById(R.id.silogBtn);
        pastilBtn = findViewById(R.id.pastilBtn);
        shawarmaBtn = findViewById(R.id.shawarmaBtn);
        sizzlingBtn = findViewById(R.id.sizzlingBtn);
        ssdBtn = findViewById(R.id.ssdBtn);
        drinkBtn = findViewById(R.id.drinkBtn);

        if (silogBtn != null)
            silogBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, SilogActivity.class)));

        if (pastilBtn != null)
            pastilBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, PastilActivity.class)));

        if (shawarmaBtn != null)
            shawarmaBtn.setOnClickListener(v ->
                    recreate());

        if (sizzlingBtn != null)
            sizzlingBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, SizzlingActivity.class)));

        if (ssdBtn != null)
            ssdBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, SSDActivity.class)));

        if (drinkBtn != null)
            drinkBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, DrinksActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}