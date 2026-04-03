package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
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

public class PastilActivity extends AppCompatActivity {

    private List<Product> pastilList;
    private RecyclerView recyclerView;
    private PastilAdapter adapter;

    // Floating cart panel
    private LinearLayout floatingCartPanel;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn;
    private MaterialButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pastil);

        // --- Back button ---
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PastilActivity.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // --- Floating cart panel ---
        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        goToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(PastilActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // --- Initialize Pastil list ---
        pastilList = new ArrayList<>();
        addPastilProducts();

        // --- Setup RecyclerView ---
        recyclerView = findViewById(R.id.pastilRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PastilAdapter(this, pastilList, () -> updateFloatingCart());
        recyclerView.setAdapter(adapter);

        // --- Initial floating panel update ---
        updateFloatingCart();
    }

    // --- Add Pastil products ---
    private void addPastilProducts() {
        DatabaseReference pastilRef = FirebaseDatabase.getInstance()
                .getReference("categories/pastil");

        String[][] pastils = {
                {"pastil_plain", "Pastil"},
                {"pastil_rice", "Pastil w/ Rice"},
                {"pastil_double_rice", "Pastil Double Rice"},
                {"double_pastil", "Double Pastil"},
                {"pastilog", "Pastilog"}
        };

        int defaultPrice = 100;
        int defaultStock = 10;

        for (String[] item : pastils) {
            String key = item[0];
            String name = item[1];
            Product product = new Product(name, defaultPrice, defaultStock);

            // Add to Firebase
            pastilRef.child(key).setValue(product);

            // Add to local list
            pastilList.add(product);
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