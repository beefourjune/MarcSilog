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

public class SSDActivity extends AppCompatActivity {

    private List<Product> ssdList;
    private RecyclerView recyclerView;
    private SSDAdapter adapter;

    // Category buttons
    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;

    // Floating cart
    private MaterialCardView floatingCartPanel;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssd);

        setupCategoryButtons();

        // Back button
        MaterialButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        // Floating cart UI
        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        goToCartBtn.setOnClickListener(v ->
                startActivity(new Intent(SSDActivity.this, CartActivity.class))
        );

        // Product list
        ssdList = new ArrayList<>();
        addSSDProducts();

        // RecyclerView
        recyclerView = findViewById(R.id.ssdRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new SSDAdapter(
                this,
                ssdList,
                this::updateFloatingCart
        );

        recyclerView.setAdapter(adapter);

        updateFloatingCart();
    }

    // ================= PRODUCTS =================
    private void addSSDProducts() {

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("categories/ssd");

        Object[][] items = {
                {"siomai_rice_4pcs", "Siomai Rice (4 pcs)", 65, R.drawable.siomairice},
                {"big_siomai_rice_3pcs", "Big Siomai Rice (3 pcs)", 75, R.drawable.bigsiomairice},
                {"dumpling_rice_3pcs", "Dumpling Rice (3 pcs)", 70, R.drawable.dumplingrice},
                {"jumbo_siopao", "Jumbo Siopao", 60, R.drawable.jumbosiopao2}
        };

        int defaultStock = 10;

        for (Object[] item : items) {

            String key = (String) item[0];
            String name = (String) item[1];
            int price = (int) item[2];
            int imageResId = (int) item[3];

            Product product = new Product(name, price, defaultStock, imageResId);

            ref.child(key).setValue(product);
            ssdList.add(product);
        }
    }

    // ================= FLOATING CART (SILOG STYLE FIX) =================
    private void updateFloatingCart() {

        int totalItems = 0;
        int totalPrice = 0;

        for (CartItem item : MainMenu.cartList) {
            totalItems++;
            totalPrice += item.price * item.quantity;
        }

        if (totalItems > 0) {
            floatingCartPanel.setVisibility(android.view.View.VISIBLE);
            cartItemCount.setText(totalItems + " items - ₱" + totalPrice);
        } else {
            floatingCartPanel.setVisibility(android.view.View.GONE);
        }
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

        if (drinkBtn != null)
            drinkBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, DrinksActivity.class)));

        // SSD button stays on same page (no recreate bug anymore)
        if (ssdBtn != null)
            ssdBtn.setOnClickListener(v ->
                    Toast.makeText(this, "Already in SSD category", Toast.LENGTH_SHORT).show()
            );
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}