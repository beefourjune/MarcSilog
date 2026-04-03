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

public class SizzlingActivity extends AppCompatActivity {

    private List<Product> sizzlingList;

    private LinearLayout floatingCartPanel;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sizzling);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SizzlingAdapter adapter = new SizzlingAdapter(
                this,
                sizzlingList,
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