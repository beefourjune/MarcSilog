package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AddOnsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button proceedOrderBtn, backBtn;
    private List<CartItem> addOnOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ons); // Create this XML layout

        recyclerView = findViewById(R.id.recyclerViewAddOns);
        proceedOrderBtn = findViewById(R.id.proceedOrderBtn);
        backBtn = findViewById(R.id.backBtn);

        // Define your Add-ons (Prices from your image)
        addOnOptions = new ArrayList<>();
        // ... (rest of the add-ons list)
        addOnOptions.add(new CartItem("Rice", 10, R.drawable.rice));
        addOnOptions.add(new CartItem("Ham", 10, R.drawable.ham));
        addOnOptions.add(new CartItem("Egg", 10, R.drawable.egg));
        addOnOptions.add(new CartItem("Hatdog", 15, R.drawable.hotdogs));
        addOnOptions.add(new CartItem("Skinless", 10, R.drawable.skinless));
        addOnOptions.add(new CartItem("Burger", 15, R.drawable.burger));
        addOnOptions.add(new CartItem("BBQ", 20, R.drawable.bbq));
        addOnOptions.add(new CartItem("Hungarian", 35, R.drawable.hungarian));
        addOnOptions.add(new CartItem("French Fries", 20, R.drawable.fries));

        // Use a modified version of your adapter or a specific AddOnAdapter
        AddOnAdapter adapter = new AddOnAdapter(
                this,
                addOnOptions,
                () -> {
                    // refresh cart badge if needed
                }
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(AddOnsActivity.this, CartActivity.class));
            finish();
        });

        proceedOrderBtn.setOnClickListener(v -> {

            if(MainMenu.cartList.isEmpty()){
                Toast.makeText(this,"Cart is empty",Toast.LENGTH_SHORT).show();
                return;
            }

            startActivity(new Intent(AddOnsActivity.this, CartActivity.class));
        });
    }
}
