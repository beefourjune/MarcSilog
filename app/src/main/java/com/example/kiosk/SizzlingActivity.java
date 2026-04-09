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

public class SizzlingActivity extends AppCompatActivity {

    private List<Product> sizzlingList;

    private MaterialCardView floatingCartPanel;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn, orderNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sizzling);

        setupCategoryButtons();

        MaterialButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SizzlingActivity.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        goToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SizzlingActivity.this, CartActivity.class);
            startActivity(intent);
        });

        sizzlingList = new ArrayList<>();
        addSizzlingProducts();

        RecyclerView recyclerView = findViewById(R.id.sizzlingRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        SizzlingAdapter adapter = new SizzlingAdapter(
                this,
                sizzlingList,
                () -> updateFloatingCart()
        );

        recyclerView.setAdapter(adapter);

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
                Intent intent = new Intent(SizzlingActivity.this, SilogActivity.class);
                startActivity(intent);
            });
        }

        if (pastilBtn != null) {
            pastilBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SizzlingActivity.this, PastilActivity.class);
                startActivity(intent);
            });
        }

        if (shawarmaBtn != null) {
            shawarmaBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SizzlingActivity.this, ShawarmaActivity.class);
                startActivity(intent);
            });
        }


        if (ssdBtn != null) {
            ssdBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SizzlingActivity.this, SSDActivity.class);
                startActivity(intent);
            });
        }

        if (drinkBtn != null) {
            drinkBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SizzlingActivity.this, DrinksActivity.class);
                startActivity(intent);
            });
        }
        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    Toast.makeText(SizzlingActivity.this, "Proceeding to Order…", Toast.LENGTH_SHORT).show()
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

    private void addSizzlingProducts() {
        DatabaseReference sizzlingRef = FirebaseDatabase.getInstance()
                .getReference("categories/sizzling");

        String[][] items = {
                {"porkchop", "Porkchop"},
                {"sisig", "Sisig"},
                {"chicken", "Chicken"},
                {"burger_steak", "Burger Steak"},
                {"kare_kare", "Kare Kare"},
                {"lechon_kawali", "Lechon Kawali"},
                {"hungarian", "Hungarian"}
        };

        int defaultPrice = 180;
        int defaultStock = 10;

        for (String[] item : items) {
            String key = item[0];
            String name = item[1];

            Product product = new Product(name, defaultPrice, defaultStock);

            sizzlingRef.child(key).setValue(product);
            sizzlingList.add(product);
        }
    }
}