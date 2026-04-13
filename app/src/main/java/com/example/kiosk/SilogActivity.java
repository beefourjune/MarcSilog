package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SilogActivity extends AppCompatActivity {

    private List<Product> silogList;
    private RecyclerView recyclerView;
    private SilogAdapter adapter;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    // Floating cart panel
    private MaterialButton goToCartBtn, orderNowBtn;
    private TextView cartItemCount;
    private MaterialButton backBtn;
    private MaterialCardView floatingCartPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silog);

        setupCategoryButtons();

        // --- Top bar back button ---
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        // --- Floating cart panel ---
        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        goToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SilogActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // --- Initialize Silog list ---
        silogList = new ArrayList<>();
        addSilogProducts();

        // --- Setup RecyclerView ---
        recyclerView = findViewById(R.id.silogRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new SilogAdapter(this, silogList, new SilogAdapter.CartUpdateListener() {
            @Override
            public void onCartUpdated() {
                updateFloatingCart();
            }
        });
        recyclerView.setAdapter(adapter);

        // --- Initial cart panel update ---
        updateFloatingCart();
    }

    // --- Add Silog products to Firebase and local list ---
    private void addSilogProducts() {
        DatabaseReference silogRef = FirebaseDatabase.getInstance()
                .getReference("categories/silog");

        String[][] silogs = {
                {"hotdog_silog", "Hotdog Silog"},
                {"ham_silog", "Ham Silog"},
                {"bologna_silog", "Bologna Silog"},
                {"longanisa_silog", "Longanisa Silog"},
                {"burger_silog", "Burger Silog"},
                {"bbq_silog", "BBQ Silog"},
                {"chicken_silog", "Chicken Silog"},
                {"pork_silog", "Pork Silog"},
                {"tapa_silog", "Tapa Silog"},
                {"tocino_silog", "Tocino Silog"},
                {"sisig_silog", "Sisig Silog"},
                {"hungarian_silog", "Hungarian Silog"}
        };

        int defaultPrice = 120;
        int defaultStock = 10;

        for (String[] item : silogs) {

            String key = item[0];
            String name = item[1];

            int price = 120; // default fallback

            switch (key) {
                case "hotdog_silog": price = 40; break;
                case "ham_silog": price = 40; break;
                case "bologna_silog": price = 45; break;
                case "longanisa_silog": price = 40; break;
                case "burger_silog": price = 40; break;
                case "bbq_silog": price = 50; break;
                case "chicken_silog": price = 60; break;
                case "pork_silog": price = 65; break;
                case "tapa_silog": price = 65; break;
                case "tocino_silog": price = 60; break;
                case "sisig_silog": price = 65; break;
                case "hungarian_silog": price = 55; break;
            }

            int imageRes;

            switch (key) {
                case "hotdog_silog": imageRes = R.drawable.hotsilog; break;
                case "ham_silog": imageRes = R.drawable.hamsilog; break;
                case "bologna_silog": imageRes = R.drawable.bologna_silog; break;
                case "longanisa_silog": imageRes = R.drawable.longanisa_silog; break;
                case "burger_silog": imageRes = R.drawable.burger_silog; break;
                case "bbq_silog": imageRes = R.drawable.bbq_silog; break;
                case "chicken_silog": imageRes = R.drawable.chicken_silog; break;
                case "pork_silog": imageRes = R.drawable.pork_silog; break;
                case "tapa_silog": imageRes = R.drawable.tapsilog; break;
                case "tocino_silog": imageRes = R.drawable.tosilog; break;
                case "sisig_silog": imageRes = R.drawable.sisig; break;
                case "hungarian_silog": imageRes = R.drawable.hungarian; break;

                default:
                    imageRes = R.drawable.baconsilog; // fallback
                    break;
            } // or your mapping

            String description;

            description = " Unli soup!";



            Product product = new Product(name, price, defaultStock, imageRes, "Silog", description);

            silogRef.child(key).setValue(product);
            silogList.add(product);
        }
    }

    // --- Update floating cart panel ---
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

    private void setupCategoryButtons() {
        silogBtn = findViewById(R.id.silogBtn);
        pastilBtn = findViewById(R.id.pastilBtn);
        shawarmaBtn = findViewById(R.id.shawarmaBtn);
        sizzlingBtn = findViewById(R.id.sizzlingBtn);
        ssdBtn = findViewById(R.id.ssdBtn);
        drinkBtn = findViewById(R.id.drinkBtn);

        if (silogBtn != null) {
            silogBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SilogActivity.this, SilogActivity.class);
                startActivity(intent);
            });
        }

        if (pastilBtn != null) {
            pastilBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SilogActivity.this, PastilActivity.class);
                startActivity(intent);
            });
        }

        if (shawarmaBtn != null) {
            shawarmaBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SilogActivity.this, ShawarmaActivity.class);
                startActivity(intent);
            });
        }

        if (sizzlingBtn != null) {
            sizzlingBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SilogActivity.this, SizzlingActivity.class);
                startActivity(intent);
            });
        }

        if (ssdBtn != null) {
            ssdBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SilogActivity.this, SSDActivity.class);
                startActivity(intent);
            });
        }

        if (drinkBtn != null) {
            drinkBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SilogActivity.this, DrinksActivity.class);
                startActivity(intent);
            });
        }
        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    Toast.makeText(SilogActivity.this, "Proceeding to Order…", Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}