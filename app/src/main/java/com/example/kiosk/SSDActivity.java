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

    private MaterialCardView floatingCartPanel;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn, orderNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssd);

        setupCategoryButtons();

        MaterialButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SSDActivity.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        floatingCartPanel = findViewById(R.id.floatingCartPanel);
        cartItemCount = findViewById(R.id.cartItemCount);
        goToCartBtn = findViewById(R.id.goToCartBtn);

        goToCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SSDActivity.this, CartActivity.class);
            startActivity(intent);
        });

        ssdList = new ArrayList<>();
        addSSDProducts();

        RecyclerView recyclerView = findViewById(R.id.ssdRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        SSDAdapter adapter = new SSDAdapter(
                this,
                ssdList,
                this::updateFloatingCart
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
            silogBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, SilogActivity.class)));
        }

        if (pastilBtn != null) {
            pastilBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, PastilActivity.class)));
        }

        if (shawarmaBtn != null) {
            shawarmaBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, ShawarmaActivity.class)));
        }

        if (sizzlingBtn != null) {
            sizzlingBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, SizzlingActivity.class)));
        }

        if (ssdBtn != null) {
            ssdBtn.setOnClickListener(v ->
                    recreate());
        }

        if (drinkBtn != null) {
            drinkBtn.setOnClickListener(v ->
                    startActivity(new Intent(this, DrinksActivity.class)));
        }

        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    Toast.makeText(this, "Proceeding to Order…", Toast.LENGTH_SHORT).show()
            );
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
            floatingCartPanel.setVisibility(android.view.View.VISIBLE);
            cartItemCount.setText(totalItems + " items - ₱" + totalPrice);
        } else {
            floatingCartPanel.setVisibility(android.view.View.GONE);
        }
    }

    private void addSSDProducts() {

        DatabaseReference ssdRef = FirebaseDatabase.getInstance()
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

            ssdRef.child(key).setValue(product);
            ssdList.add(product);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }
}