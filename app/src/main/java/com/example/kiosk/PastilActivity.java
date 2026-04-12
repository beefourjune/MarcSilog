package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PastilActivity extends AppCompatActivity {

    private List<Product> pastilList;
    private RecyclerView recyclerView;
    private PastilAdapter adapter;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    // Floating cart panel
    private MaterialCardView floatingCartPanel;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn, orderNowBtn;
    private MaterialButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pastil);

        setupCategoryButtons();

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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new PastilAdapter(this, pastilList, () -> updateFloatingCart());
        recyclerView.setAdapter(adapter);

        // --- Initial floating panel update ---
        updateFloatingCart();
    }

    // --- Add Pastil products ---
    private void addPastilProducts() {
        DatabaseReference pastilRef = FirebaseDatabase.getInstance()
                .getReference("categories/pastil");

        // ✅ Added image resource per item
        Object[][] pastils = {
                {"pastil_plain", "Pastil", 50, R.drawable.pastilog},
                {"pastil_rice", "Pastil w/ Rice", 65, R.drawable.pastilwithrice},
                {"pastil_double_rice", "Pastil Double Rice", 80, R.drawable.doublepastil},
                {"double_pastil", "Double Pastil", 90, R.drawable.doublepastil},
                {"pastilog", "Pastilog", 100, R.drawable.pastilog}
        };


        int defaultStock = 10;

        for (Object[] item : pastils) {
            String key = (String) item[0];
            String name = (String) item[1];
            int price = (int) item[2];
            int imageResId = (int) item[3];

            Product product = new Product(name, price, defaultStock, imageResId);

            // Add to Firebase
            pastilRef.child(key).setValue(product);

            // Add to local list
            pastilList.add(product);
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
                Intent intent = new Intent(PastilActivity.this, SilogActivity.class);
                startActivity(intent);
            });
        }

        if (shawarmaBtn != null) {
            shawarmaBtn.setOnClickListener(v -> {
                Intent intent = new Intent(PastilActivity.this, ShawarmaActivity.class);
                startActivity(intent);
            });
        }

        if (sizzlingBtn != null) {
            sizzlingBtn.setOnClickListener(v -> {
                Intent intent = new Intent(PastilActivity.this, SizzlingActivity.class);
                startActivity(intent);
            });
        }

        if (ssdBtn != null) {
            ssdBtn.setOnClickListener(v -> {
                Intent intent = new Intent(PastilActivity.this, SSDActivity.class);
                startActivity(intent);
            });
        }

        if (drinkBtn != null) {
            drinkBtn.setOnClickListener(v -> {
                Intent intent = new Intent(PastilActivity.this, DrinksActivity.class);
                startActivity(intent);
            });
        }

        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    Toast.makeText(PastilActivity.this, "Proceeding to Order…", Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}