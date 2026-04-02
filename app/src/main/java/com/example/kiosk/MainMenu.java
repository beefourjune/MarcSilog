package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    // --- Firebase ---
    private DatabaseReference database;
    private DatabaseReference productsRef;

    // --- Cart UI ---
    private TextView cartInfo;
    private View cartPanel;
    private MaterialButton viewCartBtn;
    private LinearLayout productContainer;

    // --- Category buttons ---
    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;
    private MaterialButton orderNowBtn, cartBtn, backBtn;

    // --- Add-to-cart buttons ---
    private MaterialButton addBaconBtn, addTapBtn, addTosilogBtn, addBurgerBtn, addShawarmaBtn,
            addSiomaiBtn, addBigSiomaiBtn, addDumplingBtn, addSiopaoBtn;

    // --- Cart data ---
    public static ArrayList<CartItem> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // --- Firebase init ---
        database = FirebaseDatabase.getInstance().getReference();
        productsRef = database.child("products");

        // --- Initialize default products in Firebase ---
        initializeDefaultProducts();

        // --- UI init ---
        cartPanel = findViewById(R.id.cartPanel);
        cartInfo = findViewById(R.id.cartInfo);
        viewCartBtn = findViewById(R.id.viewCartBtn);
        productContainer = findViewById(R.id.productContainer);

        setupCartUI();
        setupCategoryButtons();
        setupAddToCartButtons();

        // --- Refresh cart initially ---
        refreshCartInfo();
    }

    // --- Initialize default products in Firebase ---
    private void initializeDefaultProducts() {
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                addProductIfMissing(snapshot, "BaconSilog", 120, 10, R.drawable.baconsilog);
                addProductIfMissing(snapshot, "TapSilog", 60, 8, R.drawable.tapsilog);
                addProductIfMissing(snapshot, "ToSilog", 80, 5, R.drawable.tosilog);
                addProductIfMissing(snapshot, "BurgerSteak", 75, 6, R.drawable.burgersteak);
                addProductIfMissing(snapshot, "Shawarma", 85, 7, R.drawable.shawarmacute);
                addProductIfMissing(snapshot, "Siomai", 50, 12, R.drawable.siomairice);
                addProductIfMissing(snapshot, "BigSiomai", 70, 10, R.drawable.bigsiomairice);
                addProductIfMissing(snapshot, "Dumpling", 60, 8, R.drawable.dumplingrice);
                addProductIfMissing(snapshot, "JumboSiopao", 45, 15, R.drawable.jumbosiopao);

                showToast("Default products initialized!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Firebase error: " + error.getMessage());
            }
        });
    }

    // --- Add product if missing ---
    private void addProductIfMissing(DataSnapshot snapshot, String name, int price, int stock, int imageResId) {
        if (!snapshot.hasChild(name)) {
            Product product = new Product(name, price, stock, imageResId);
            productsRef.child(name).setValue(product);
        }
    }

    // --- Setup cart UI ---
    private void setupCartUI() {
        if (cartPanel != null) {
            cartPanel.setVisibility(View.GONE);
            cartPanel.setOnClickListener(v -> openCart());
        }
        if (viewCartBtn != null) viewCartBtn.setOnClickListener(v -> openCart());

        backBtn = findViewById(R.id.backtostartbtn);
        if (backBtn != null) backBtn.setOnClickListener(v -> finish());

        cartBtn = findViewById(R.id.cartBtn);
        if (cartBtn != null) cartBtn.setOnClickListener(v -> openCart());
    }

    // --- Setup category buttons ---
    private void setupCategoryButtons() {
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

        orderNowBtn = findViewById(R.id.orderNowBtn);
        if (orderNowBtn != null) orderNowBtn.setOnClickListener(v -> showToast("Proceeding to Order…"));
    }

    // --- Setup add-to-cart buttons ---
    private void setupAddToCartButtons() {
        addBaconBtn = findViewById(R.id.addBaconBtn);
        addTapBtn = findViewById(R.id.addTapBtn);
        addTosilogBtn = findViewById(R.id.addTosilogBtn);
        addBurgerBtn = findViewById(R.id.addBurgerBtn);
        addShawarmaBtn = findViewById(R.id.addShawarmaBtn);
        addSiomaiBtn = findViewById(R.id.addSiomaiBtn);
        addBigSiomaiBtn = findViewById(R.id.addBigSiomaiBtn);
        addDumplingBtn = findViewById(R.id.addDumplingBtn);
        addSiopaoBtn = findViewById(R.id.addSiopaoBtn);

        setAddToCartListener(addBaconBtn, "BaconSilog", 120, R.drawable.baconsilog);
        setAddToCartListener(addTapBtn, "TapSilog", 60, R.drawable.tapsilog);
        setAddToCartListener(addTosilogBtn, "ToSilog", 80, R.drawable.tosilog);
        setAddToCartListener(addBurgerBtn, "BurgerSteak", 75, R.drawable.burgersteak);
        setAddToCartListener(addShawarmaBtn, "Shawarma", 85, R.drawable.shawarmacute);
        setAddToCartListener(addSiomaiBtn, "Siomai", 50, R.drawable.siomairice);
        setAddToCartListener(addBigSiomaiBtn, "BigSiomai", 70, R.drawable.bigsiomairice);
        setAddToCartListener(addDumplingBtn, "Dumpling", 60, R.drawable.dumplingrice);
        setAddToCartListener(addSiopaoBtn, "JumboSiopao", 45, R.drawable.jumbosiopao);
    }

    private void setAddToCartListener(MaterialButton button, String name, int price, int imageResId) {
        if (button != null) button.setOnClickListener(v -> addItemToCart(name, price, imageResId));
    }

    // --- Add to cart ---
    private void addItemToCart(String name, int price, int imageResId) {
        cartList.add(new CartItem(name, price, imageResId));
        if (cartPanel.getVisibility() == View.GONE) cartPanel.setVisibility(View.VISIBLE);
        refreshCartInfo();
        showToast(name + " added to cart");
    }

    // --- Refresh cart info ---
    private void refreshCartInfo() {
        int totalItems = 0;
        int totalPrice = 0;
        for (CartItem item : cartList) {
            totalItems++;
            totalPrice += item.price;
        }

        if (cartInfo != null) {
            if (totalItems > 0) {
                cartInfo.setText(totalItems + " items - ₱" + totalPrice);
                cartPanel.setVisibility(View.VISIBLE);
            } else {
                cartInfo.setText("Cart is empty");
                cartPanel.setVisibility(View.GONE);
            }
        }
    }

    // --- Open cart activity ---
    private void openCart() {
        Intent intent = new Intent(MainMenu.this, CartActivity.class);
        intent.putParcelableArrayListExtra("cart_items", new ArrayList<>(cartList));
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCartInfo();
    }
}