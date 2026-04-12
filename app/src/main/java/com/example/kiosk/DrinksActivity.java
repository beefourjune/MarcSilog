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

public class DrinksActivity extends AppCompatActivity {

    private List<Product> drinksList;

    private MaterialCardView floatingCartPanel;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        setupCategoryButtons();

        MaterialButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DrinksActivity.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        goToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DrinksActivity.this, CartActivity.class);
            startActivity(intent);
        });

        drinksList = new ArrayList<>();
        addDrinksProducts();

        RecyclerView recyclerView = findViewById(R.id.drinksRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        DrinksAdapter adapter = new DrinksAdapter(
                this,
                drinksList,
                this::updateFloatingCart
        );

        recyclerView.setAdapter(adapter);

        updateFloatingCart();
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
            silogBtn.setOnClickListener(v -> startActivity(new Intent(this, SilogActivity.class)));

        if (pastilBtn != null)
            pastilBtn.setOnClickListener(v -> startActivity(new Intent(this, PastilActivity.class)));

        if (shawarmaBtn != null)
            shawarmaBtn.setOnClickListener(v -> startActivity(new Intent(this, ShawarmaActivity.class)));

        if (sizzlingBtn != null)
            sizzlingBtn.setOnClickListener(v -> startActivity(new Intent(this, SizzlingActivity.class)));

        if (ssdBtn != null)
            ssdBtn.setOnClickListener(v -> startActivity(new Intent(this, SSDActivity.class)));
    }

    // ================= FLOATING CART FIX =================
    private void updateFloatingCart() {

        int totalItems = 0;
        int totalPrice = 0;

        if (MainMenu.cartList == null) return;

        for (CartItem item : MainMenu.cartList) {
            totalItems += item.getQuantity(); // FIXED
            totalPrice += item.getPrice() * item.getQuantity(); // FIXED
        }

        if (floatingCartPanel == null) return;

        if (totalItems > 0) {
            floatingCartPanel.setVisibility(android.view.View.VISIBLE);
            cartItemCount.setText(totalItems + " items - ₱" + totalPrice);
        } else {
            floatingCartPanel.setVisibility(android.view.View.GONE);
        }
    }

    // ================= PRODUCTS =================
    private void addDrinksProducts() {

        DatabaseReference drinksRef = FirebaseDatabase.getInstance()
                .getReference("categories/drinks");

        Object[][] items = {
                {"mineral_1l", "Mineral 1L", 25, R.drawable.water},
                {"mineral_500ml", "Mineral 500ML", 15, R.drawable.water},
                {"mismo", "Mismo", 20, R.drawable.coke},
                {"mountain_dew", "Mountain Dew", 35, R.drawable.mountaindew},
                {"sting_red", "Sting Red", 30, R.drawable.sting},
                {"cobra", "Cobra", 40, R.drawable.cobra},
                {"juice_1_5l", "1.5 Juice", 45, R.drawable.juice}
        };

        int defaultStock = 20;

        for (Object[] item : items) {

            String key = (String) item[0];
            String name = (String) item[1];
            int price = (int) item[2];
            int imageResId = (int) item[3];

            Product product = new Product(name, price, defaultStock, imageResId);

            drinksRef.child(key).setValue(product);
            drinksList.add(product);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}