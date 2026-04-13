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

public class SilogActivity extends AppCompatActivity {

    private List<Product> silogList;
    private RecyclerView recyclerView;
    private SilogAdapter adapter;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    // Floating cart panel
    private MaterialButton goToCartBtn;
    private TextView cartItemCount;
    private MaterialButton backBtn;
    private MaterialCardView floatingCartPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silog);

        // ================= CATEGORY BUTTONS =================
        setupCategoryButtons();

        // ================= BACK BUTTON =================
        backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        // ================= FLOATING CART =================
        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        if (goToCartBtn != null) {
            goToCartBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SilogActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }

        // ================= SILOG PRODUCTS =================
        silogList = new ArrayList<>();
        addSilogProducts();

        recyclerView = findViewById(R.id.silogRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new SilogAdapter(this, silogList, () -> updateFloatingCart());
        recyclerView.setAdapter(adapter);

        updateFloatingCart();
    }

    // ================= ADD PRODUCTS =================
    private void addSilogProducts() {

        DatabaseReference silogRef =
                FirebaseDatabase.getInstance().getReference("categories/silog");

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
        String category = "Silog";

        for (Object[] item : silogs) {

            String key = (String) item[0];
            String name = (String) item[1];
            int price = (int) item[2];
            int imageResId = (int) item[3];

            Product product = new Product(name, price, defaultStock, imageResId, category);

            silogRef.child(key).setValue(product);
            silogList.add(product);
        }
    }

    // ================= FLOATING CART =================
    private void updateFloatingCart() {

        int uniqueItems = MainMenu.cartList.size();
        int totalPrice = 0;

        for (CartItem item : MainMenu.cartList) {
            totalPrice += item.price * item.quantity;
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
            silogBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, SilogActivity.class)));

        if (pastilBtn != null)
            pastilBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, PastilActivity.class)));

        if (shawarmaBtn != null)
            shawarmaBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, ShawarmaActivity.class)));

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