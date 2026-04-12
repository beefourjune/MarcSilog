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

    private void addSilogProducts() {
        DatabaseReference silogRef = FirebaseDatabase.getInstance()
                .getReference("categories/silog");

        // ✅ updated to include images
        Object[][] silogs = {
                {"hotdog_silog", "Hotdog Silog", 95, R.drawable.hotsilog},
                {"ham_silog", "Ham Silog", 100, R.drawable.hamsilog},
                {"bologna_silog", "Bologna Silog", 105, R.drawable.bologna_silog},
                {"longanisa_silog", "Longanisa Silog", 110, R.drawable.longanisa_silog},
                {"burger_silog", "Burger Silog", 115, R.drawable.burger_silog},
                {"bbq_silog", "BBQ Silog", 120, R.drawable.bbq_silog},
                {"chicken_silog", "Chicken Silog", 125, R.drawable.chicken_silog},
                {"pork_silog", "Pork Silog", 130, R.drawable.pork_silog},
                {"tapa_silog", "Tapa Silog", 135, R.drawable.tapsilog},
                {"tocino_silog", "Tocino Silog", 120, R.drawable.tosilog},
                {"sisig_silog", "Sisig Silog", 140, R.drawable.sisig},
                {"hungarian_silog", "Hungarian Silog", 150, R.drawable.hungarian_silog},
                {"bacon_silog", "Bacon Silog", 120, R.drawable.baconsilog}

        };

        int defaultStock = 10;

        for (Object[] item : silogs) {
            String key = (String) item[0];
            String name = (String) item[1];
            int price = (int) item[2];
            int imageResId = (int) item[3];

            Product product = new Product(name, price, defaultStock, imageResId);

            // Add to Firebase (optional overwrite)
            silogRef.child(key).setValue(product);

            // Add to local list
            silogList.add(product);
        }
    }

    // --- Update floating cart panel ---
    private void updateFloatingCart() {

        int totalItems = 0;
        int totalPrice = 0;

        for (CartItem item : MainMenu.cartList) {
            totalItems++;
            totalPrice += item.price;
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