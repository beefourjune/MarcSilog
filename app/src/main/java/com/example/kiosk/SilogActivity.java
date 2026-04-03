package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SilogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silog);

        // Back button
        MaterialButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SilogActivity.this, MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Add Silog products to Firebase with fixed keys
        addSilogProducts();
    }

    private void addSilogProducts() {
        DatabaseReference silogRef = FirebaseDatabase.getInstance()
                .getReference("categories/silog");

        // Silog items and their keys
        String[][] silogs = {
                {"hotdog_silog", "Hotdog Silog"},
                {"ham_silog", "Ham Silog"},
                {"bologna_silog", "Bologna Silog"},
                {"longanisa_silog", "Longanisa Silog"},
                {"burger_silog", "Burger Silog"},
                {"bbq_silog", "BBQ Silog"},
                {"chicken_silog", "Chicken Silog"},
                {"pork_silog", "Pork Silog"},
                {"tapa_silog", "Tapa Silog"},
                {"tocino_silog", "Tocino Silog"},
                {"sisig_silog", "Sisig Silog"},
                {"hungarian_silog", "Hungarian Silog"}
        };

        int defaultPrice = 120;
        int defaultStock = 10;

        for (String[] item : silogs) {
            String key = item[0];    // fixed key
            String name = item[1];   // product name
            Product product = new Product(name, defaultPrice, defaultStock);

            // Set value with fixed key (overwrites if exists)
            silogRef.child(key).setValue(product);
        }
    }
}