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

public class DrinksActivity extends AppCompatActivity {

    private List<Product> drinksList;

    private MaterialCardView floatingCartPanel;

    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;
    private TextView cartItemCount;
    private MaterialButton goToCartBtn,orderNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

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
                Intent intent = new Intent(DrinksActivity.this, SilogActivity.class);
                startActivity(intent);
            });
        }

        if (pastilBtn != null) {
            pastilBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DrinksActivity.this, PastilActivity.class);
                startActivity(intent);
            });
        }

        if (shawarmaBtn != null) {
            shawarmaBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DrinksActivity.this, ShawarmaActivity.class);
                startActivity(intent);
            });
        }

        if (sizzlingBtn != null) {
            sizzlingBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DrinksActivity.this, SizzlingActivity.class);
                startActivity(intent);
            });
        }

        if (ssdBtn != null) {
            ssdBtn.setOnClickListener(v -> {
                Intent intent = new Intent(DrinksActivity.this, DrinksActivity.class);
                startActivity(intent);
            });
        }
        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    Toast.makeText(DrinksActivity.this, "Proceeding to Order…", Toast.LENGTH_SHORT).show()
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

    private void addDrinksProducts() {

        DatabaseReference drinksRef = FirebaseDatabase.getInstance()
                .getReference("categories/drinks");

        int defaultStock = 20;

        Product mineral1L = new Product(
                "Mineral",
                20,
                defaultStock,
                R.drawable.glasswater,
                "Drinks",
                "1L"
        );

        Product mineral500ml = new Product(
                "Mineral",
                15,
                defaultStock,
                R.drawable.water,
                "Drinks",
                "500ml"
        );

        Product mismo = new Product(
                "Mismo",
                25,
                defaultStock,
                R.drawable.coke,
                "Drinks",
                "Coke"
        );

        Product mountainDew = new Product(
                "Mountain Dew",
                25,
                defaultStock,
                R.drawable.mountaindew,
                "Drinks",
                ""
        );

        Product stingRed = new Product(
                "Sting Red",
                25,
                defaultStock,
                R.drawable.sting,
                "Drinks",
                ""
        );

        Product cobra = new Product(
                "Cobra",
                35,
                defaultStock,
                R.drawable.cobra,
                "Drinks",
                ""
        );

        Product juice15L = new Product(
                "Juice",
                39,
                defaultStock,
                R.drawable.juice,
                "Drinks",
                "1.5"
        );

        drinksRef.child("mineral_1l").setValue(mineral1L);
        drinksRef.child("mineral_500ml").setValue(mineral500ml);
        drinksRef.child("mismo").setValue(mismo);
        drinksRef.child("mountain_dew").setValue(mountainDew);
        drinksRef.child("sting_red").setValue(stingRed);
        drinksRef.child("cobra").setValue(cobra);
        drinksRef.child("juice_1_5l").setValue(juice15L);

        drinksList.add(mineral1L);
        drinksList.add(mineral500ml);
        drinksList.add(mismo);
        drinksList.add(mountainDew);
        drinksList.add(stingRed);
        drinksList.add(cobra);
        drinksList.add(juice15L);
    }
}