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

public class PastilActivity extends AppCompatActivity {

    private List<Product> pastilList;
    private RecyclerView recyclerView;
    private PastilAdapter adapter;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    // Floating cart panel
    private MaterialCardView floatingCartPanel;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn;
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

        // --- Initialize list ---
        pastilList = new ArrayList<>();
        addPastilProducts();

        // --- Recycler ---
        recyclerView = findViewById(R.id.pastilRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new PastilAdapter(
                this,
                pastilList,
                this::updateFloatingCart,
                getSupportFragmentManager()
        );

        recyclerView.setAdapter(adapter);

        updateFloatingCart();
    }

    // ================= PRODUCTS =================
    private void addPastilProducts() {

        DatabaseReference pastilRef = FirebaseDatabase.getInstance()
                .getReference("categories/pastil");

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

            pastilRef.child(key).setValue(product);
            pastilList.add(product);
        }
    }

    // ================= FLOATING CART FIX (UNIQUE COUNT) =================
    private void updateFloatingCart() {

        if (MainMenu.cartList == null) return;

        int uniqueItems = MainMenu.cartList.size();
        int totalPrice = 0;

        for (CartItem item : MainMenu.cartList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        if (floatingCartPanel == null) return;

        if (uniqueItems > 0) {
            floatingCartPanel.setVisibility(android.view.View.VISIBLE);
            cartItemCount.setText(getString(R.string.cart_items_format, uniqueItems, totalPrice));
        } else {
            floatingCartPanel.setVisibility(android.view.View.GONE);
        }
    }

    // ================= CATEGORY NAVIGATION =================
    private void setupCategoryButtons() {

        silogBtn = findViewById(R.id.silogBtn);
        pastilBtn = findViewById(R.id.pastilBtn);
        shawarmaBtn = findViewById(R.id.shawarmaBtn);
        sizzlingBtn = findViewById(R.id.sizzlingBtn);
        ssdBtn = findViewById(R.id.ssdBtn);
        drinkBtn = findViewById(R.id.drinkBtn);

        if (silogBtn != null)
            silogBtn.setOnClickListener(v -> startActivity(new Intent(this, SilogActivity.class)));

        if (shawarmaBtn != null)
            shawarmaBtn.setOnClickListener(v -> startActivity(new Intent(this, ShawarmaActivity.class)));

        if (sizzlingBtn != null)
            sizzlingBtn.setOnClickListener(v -> startActivity(new Intent(this, SizzlingActivity.class)));

        if (ssdBtn != null)
            ssdBtn.setOnClickListener(v -> startActivity(new Intent(this, SSDActivity.class)));

        if (drinkBtn != null)
            drinkBtn.setOnClickListener(v -> startActivity(new Intent(this, DrinksActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}