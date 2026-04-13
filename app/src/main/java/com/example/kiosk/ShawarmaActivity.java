package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class ShawarmaActivity extends AppCompatActivity {

    private List<Product> shawarmaList;

    private MaterialCardView floatingCartPanel;
    private TextView cartItemCount;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    private MaterialButton goToCartBtn,orderNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shawarma);

        setupCategoryButtons();
        
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ShawarmaAdapter adapter = new ShawarmaAdapter(this, shawarmaList, () -> updateFloatingCart());
        recyclerView.setAdapter(adapter);

        // Initial update
        updateFloatingCart();
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
                Intent intent = new Intent(ShawarmaActivity.this, SilogActivity.class);
                startActivity(intent);
            });
        }

        if (pastilBtn != null) {
            pastilBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ShawarmaActivity.this, PastilActivity.class);
                startActivity(intent);
            });
        }

        if (sizzlingBtn != null) {
            sizzlingBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ShawarmaActivity.this, SizzlingActivity.class);
                startActivity(intent);
            });
        }

        if (ssdBtn != null) {
            ssdBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ShawarmaActivity.this, SSDActivity.class);
                startActivity(intent);
            });
        }

        if (drinkBtn != null) {
            drinkBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ShawarmaActivity.this, DrinksActivity.class);
                startActivity(intent);
            });
        }
        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    Toast.makeText(ShawarmaActivity.this, "Proceeding to Order…", Toast.LENGTH_SHORT).show()
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }

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

    private void addShawarmaProducts() {

        DatabaseReference shawarmaRef = FirebaseDatabase.getInstance()
                .getReference("categories/shawarma");

        int defaultStock = 10;
        String defaultdesc = "Unli gravy!";

        // ✅ EACH PRODUCT HAS ITS OWN PRICE

        Product shawarmaRice = new Product(
                "Shawarma Rice",
                39,
                defaultStock,
                R.drawable.shawarmarice,
                "Shawarma",
                defaultdesc
        );

        Product shawarmaDoubleRice = new Product(
                "Shawarma Double Rice",
                49,
                defaultStock,
                R.drawable.shawarmarice,
                "Shawarma",
                defaultdesc
        );

        Product shawarmaBurger = new Product(
                "Shawarma Burger",
                35,
                defaultStock,
                R.drawable.shawarmaburger,
                "Shawarma",
                defaultdesc
        );

        Product shawarmaBurgerFries = new Product(
                "Shawarma Burger w/ Fries",
                55,
                defaultStock,
                R.drawable.shawarmaburger,
                "Shawarma",
                defaultdesc
        );

        Product shawarmaPita = new Product(
                "Shawarma Pita",
                35,
                defaultStock,
                R.drawable.shawarmapita,
                "Shawarma",
                defaultdesc
        );

        // Save to Firebase
        shawarmaRef.child("shawarma_rice").setValue(shawarmaRice);
        shawarmaRef.child("shawarma_double_rice").setValue(shawarmaDoubleRice);
        shawarmaRef.child("shawarma_burger").setValue(shawarmaBurger);
        shawarmaRef.child("shawarma_burger_fries").setValue(shawarmaBurgerFries);
        shawarmaRef.child("shawarma_pita").setValue(shawarmaPita);

        // Add to list
        shawarmaList.add(shawarmaRice);
        shawarmaList.add(shawarmaDoubleRice);
        shawarmaList.add(shawarmaBurger);
        shawarmaList.add(shawarmaBurgerFries);
        shawarmaList.add(shawarmaPita);
    }
}