package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    MaterialButton backBtn, orderNowBtn, cartBtn;
    ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;


    public static ArrayList<String> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //  BACK BUTTON
        backBtn = findViewById(R.id.backtostartbtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }

        //  CART BUTTON
        cartBtn = findViewById(R.id.cartBtn);
        if (cartBtn != null) {
            cartBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainMenu.this, CartActivity.class);
                intent.putStringArrayListExtra("cart", cartList);
                startActivity(intent);
            });
        }

        //  CATEGORY BUTTONS
        silogBtn = findViewById(R.id.silogBtn);
        pastilBtn = findViewById(R.id.pastilBtn);
        shawarmaBtn = findViewById(R.id.shawarmaBtn);
        sizzlingBtn = findViewById(R.id.sizzlingBtn);
        ssdBtn = findViewById(R.id.ssdBtn);
        drinkBtn = findViewById(R.id.drinkBtn);

        if (silogBtn != null) silogBtn.setOnClickListener(v -> showToast("Silog clicked"));
        if (pastilBtn != null) pastilBtn.setOnClickListener(v -> showToast("Pastil clicked"));
        if (shawarmaBtn != null) shawarmaBtn.setOnClickListener(v -> showToast("Shawarma clicked"));
        if (sizzlingBtn != null) sizzlingBtn.setOnClickListener(v -> showToast("Sizzling clicked"));
        if (ssdBtn != null) ssdBtn.setOnClickListener(v -> showToast("SSD clicked"));
        if (drinkBtn != null) drinkBtn.setOnClickListener(v -> showToast("Drinks clicked"));

        //  ADD TO CART BUTTONS
        setupAddButton(R.id.addBaconBtn, "BaconSilog");
        setupAddButton(R.id.addTapBtn, "TapSilog");
        setupAddButton(R.id.addTosilogBtn, "ToSilog");
        setupAddButton(R.id.addBurgerBtn, "Burger Steak");
        setupAddButton(R.id.addShawarmaBtn, "Shawarma");

        setupAddButton(R.id.addSiomaiBtn, "Siomai Rice");
        setupAddButton(R.id.addBigSiomaiBtn, "Big Siomai");
        setupAddButton(R.id.addDumplingBtn, "Dumpling Rice");
        setupAddButton(R.id.addSiopaoBtn, "Siopao");

        //  ORDER BUTTON
        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) {
            orderNowBtn.setOnClickListener(v ->
                    showToast("Proceeding to Order...")
            );
        }
    }

    //  ADD ITEM TO CART
    private void setupAddButton(int id, String itemName) {
        MaterialButton btn = findViewById(id);

        if (btn != null) {
            btn.setOnClickListener(v -> {
                cartList.add(itemName);
                Toast.makeText(MainMenu.this,
                        itemName + " added to cart (" + cartList.size() + ")",
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    //
    private void showToast(String message) {
        Toast.makeText(MainMenu.this, message, Toast.LENGTH_SHORT).show();
    }
}