package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShawarmaActivity extends AppCompatActivity {

    private List<Product> shawarmaList;

    private LinearLayout floatingCartPanel;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shawarma);

        // Back button
        MaterialButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ShawarmaActivity.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Floating cart panel
        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        goToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ShawarmaActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Initialize list
        shawarmaList = new ArrayList<>();

        // Add Shawarma products
        addShawarmaProducts();

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.shawarmaRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ShawarmaAdapter adapter = new ShawarmaAdapter(this, shawarmaList, () -> updateFloatingCart());
        recyclerView.setAdapter(adapter);

        // Initial update
        updateFloatingCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }

    private void updateFloatingCart() {
        int totalItems = MainMenu.cartList.size();

        if (totalItems > 0) {
            floatingCartPanel.setVisibility(View.VISIBLE);
            cartItemCount.setText("Cart: " + totalItems + " items");
        } else {
            floatingCartPanel.setVisibility(View.GONE);
        }
    }

    private void addShawarmaProducts() {
        DatabaseReference shawarmaRef = FirebaseDatabase.getInstance()
                .getReference("categories/shawarma");

        String[][] shawarmas = {
                {"shawarma_rice", "Shawarma Rice"},
                {"shawarma_double_rice", "Shawarma Double Rice"},
                {"shawarma_burger", "Shawarma Burger"},
                {"shawarma_burger_fries", "Shawarma Burger w/ Fries"},
                {"shawarma_pita", "Shawarma Pita"}
        };

        int defaultPrice = 150;
        int defaultStock = 10;

        for (String[] item : shawarmas) {
            String key = item[0];
            String name = item[1];

            Product product = new Product(name, defaultPrice, defaultStock);

            // Save to Firebase
            shawarmaRef.child(key).setValue(product);

            // Add locally for RecyclerView
            shawarmaList.add(product);
        }
    }
}