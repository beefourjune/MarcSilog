package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class ShawarmaActivity extends AppCompatActivity {

    private List<Product> shawarmaList;

    private MaterialCardView floatingCartPanel;
    private TextView cartItemCount;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    private MaterialButton goToCartBtn, orderNowBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shawarma);

        setupCategoryButtons();

        // Back button
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

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
        addShawarmaProducts();

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.shawarmaRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ShawarmaAdapter adapter = new ShawarmaAdapter(
                this,
                shawarmaList,
                this::updateFloatingCart
        );

        recyclerView.setAdapter(adapter);

        updateFloatingCart();
    }

    private void addShawarmaProducts() {

        DatabaseReference shawarmaRef = FirebaseDatabase.getInstance()
                .getReference("categories/shawarma");

        Object[][] shawarmas = {
                {"shawarma_rice", "Shawarma Rice", 120, R.drawable.shawarmarice},
                {"shawarma_double_rice", "Shawarma Double Rice", 140, R.drawable.shawarmarice},
                {"shawarma_burger", "Shawarma Burger", 90, R.drawable.shawarmaburger},
                {"shawarma_burger_fries", "Shawarma Burger w/ Fries", 110, R.drawable.shawarma_burger_fries},
                {"shawarma_pita", "Shawarma Pita", 80, R.drawable.shawarmapita}
        };
        int defaultStock = 10;

        for (Object[] item : shawarmas) {

            String key = (String) item[0];
            String name = (String) item[1];
            int price = (int) item[2];
            int imageResId = (int) item[3];

            Product product = new Product(name, price, defaultStock, imageResId);

            shawarmaRef.child(key).setValue(product);
            shawarmaList.add(product);
        }
    }

    private void updateFloatingCart() {

        int totalItems = 0;
        int totalPrice = 0;

        for (CartItem item : MainMenu.cartList) {

            totalItems++;
            totalPrice += item.price;
        }

        if (totalItems > 0) {
            floatingCartPanel.setVisibility(View.VISIBLE);
            cartItemCount.setText(totalItems + " Items - ₱" + totalPrice);
        } else {
            floatingCartPanel.setVisibility(View.GONE);
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
                Intent intent = new Intent(this, SilogActivity.class);
                startActivity(intent);
            });
        }

        if (pastilBtn != null) {
            pastilBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, PastilActivity.class);
                startActivity(intent);
            });
        }

        if (shawarmaBtn != null) {
            shawarmaBtn.setOnClickListener(v -> recreate());
        }

        if (sizzlingBtn != null) {
            sizzlingBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, SizzlingActivity.class);
                startActivity(intent);
            });
        }

        if (ssdBtn != null) {
            ssdBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, SSDActivity.class);
                startActivity(intent);
            });
        }

        if (drinkBtn != null) {
            drinkBtn.setOnClickListener(v -> {
                Intent intent = new Intent(this, DrinksActivity.class);
                startActivity(intent);
            });
        }

        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    Toast.makeText(this, "Proceeding to Order…", Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}