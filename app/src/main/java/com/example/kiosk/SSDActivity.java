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
                Intent intent = new Intent(SSDActivity.this, SilogActivity.class);
                startActivity(intent);
            });
        }

        if (pastilBtn != null) {
            pastilBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SSDActivity.this, PastilActivity.class);
                startActivity(intent);
            });
        }

        if (shawarmaBtn != null) {
            shawarmaBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SSDActivity.this, ShawarmaActivity.class);
                startActivity(intent);
            });
        }

        if (sizzlingBtn != null) {
            sizzlingBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SSDActivity.this, SizzlingActivity.class);
                startActivity(intent);
            });
        }

        if (drinkBtn != null) {
            drinkBtn.setOnClickListener(v -> {
                Intent intent = new Intent(SSDActivity.this, DrinksActivity.class);
                startActivity(intent);
            });
        }
        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    Toast.makeText(SSDActivity.this, "Proceeding to Order…", Toast.LENGTH_SHORT).show()
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

    private void addSSDProducts() {
        DatabaseReference ssdRef = FirebaseDatabase.getInstance()
                .getReference("categories/ssd");

        String[][] items = {
                {"siomai_rice_4pcs", "Siomai Rice (4 pcs)"},
                {"big_siomai_rice_3pcs", "Big Siomai Rice (3 pcs)"},
                {"dumpling_rice_3pcs", "Dumpling Rice (3 pcs)"},
                {"jumbo_siopao", "Jumbo Siopao"}
        };

        int defaultPrice = 90;
        int defaultStock = 10;

        for (String[] item : items) {
            Product product = new Product(
                    item[1],
                    defaultPrice,
                    defaultStock
            );

            ssdRef.child(item[0]).setValue(product);
            ssdList.add(product);
        }
    }
}