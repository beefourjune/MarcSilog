package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SilogActivity extends AppCompatActivity {

    private List<Product> silogList;
    private RecyclerView recyclerView;
    private SilogAdapter adapter;

    // Floating cart panel
    private MaterialButton goToCartBtn;
    private TextView cartItemCount;
    private MaterialButton backBtn;
    private android.widget.LinearLayout floatingCartPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silog);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            Product product = new Product(name, defaultPrice, defaultStock);

            // Add to Firebase (optional overwrite)
            silogRef.child(key).setValue(product);

            // Add to local list
            silogList.add(product);
        }
    }

    // --- Update floating cart panel ---
    private void updateFloatingCart() {
        int totalItems = MainMenu.cartList.size();
        if (totalItems > 0) {
            floatingCartPanel.setVisibility(android.view.View.VISIBLE);
            cartItemCount.setText("Cart: " + totalItems + " items");
        } else {
            floatingCartPanel.setVisibility(android.view.View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}