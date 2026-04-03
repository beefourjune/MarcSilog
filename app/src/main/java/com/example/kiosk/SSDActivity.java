package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SSDActivity extends AppCompatActivity {

    private List<Product> ssdList;

    private LinearLayout floatingCartPanel;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssd);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SSDAdapter adapter = new SSDAdapter(
                this,
                ssdList,
                () -> updateFloatingCart()
        );

        recyclerView.setAdapter(adapter);

        updateFloatingCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFloatingCart();
    }

    private void updateFloatingCart() {
        int totalItems = MainMenu.cartList.size();

        if (totalItems > 0) {
            floatingCartPanel.setVisibility(View.VISIBLE);
            cartItemCount.setText("Cart: " + totalItems + " items");
        } else {
            floatingCartPanel.setVisibility(View.GONE);
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