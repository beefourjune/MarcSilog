package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

    // 🔥 Cached product list
    private ArrayList<Product> fullProductList = new ArrayList<>();

    // --- UI ---
    private TextView cartInfo;
    private View cartPanel;
    private MaterialButton viewCartBtn;
    private LinearLayout productContainer;
    private EditText searchBar;

    // --- Category buttons ---
    private ImageButton silogBtn, pastilBtn, shawarmaBtn, sizzlingBtn, ssdBtn, drinkBtn;
    private MaterialButton orderNowBtn, cartBtn, backBtn;

    // --- Add to cart ---
    private MaterialButton addBaconBtn, addTapBtn, addTosilogBtn, addBurgerBtn, addShawarmaBtn,
            addSiomaiBtn, addBigSiomaiBtn, addDumplingBtn, addSiopaoBtn;

    // --- Cart ---
    public static ArrayList<CartItem> cartList = new ArrayList<>();
    public static ArrayList<CartItem> cartListBackup = new ArrayList<>();

    public static String orderType = "TAKE OUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        database = FirebaseDatabase.getInstance().getReference();
        productsRef = database.child("products");

        loadAllProducts();

        // ================= FIXED ORDER TYPE HANDLING =================
        if (getIntent() != null) {
            String incomingType = getIntent().getStringExtra("order_type");

            if (incomingType != null && !incomingType.isEmpty()) {
                orderType = incomingType;
            }
        }

        initializeDefaultProducts();

        cartPanel = findViewById(R.id.cartPanel);
        cartInfo = findViewById(R.id.cartInfo);
        viewCartBtn = findViewById(R.id.viewCartBtn);
        productContainer = findViewById(R.id.productContainer);
        searchBar = findViewById(R.id.searchBar);

        setupSearch();
        setupCartUI();
        setupCategoryButtons();
        setupAddToCartButtons();

        refreshCartInfo();
    }

    // ================= LOAD FIREBASE PRODUCTS =================
    private void loadAllProducts() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                fullProductList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product product = ds.getValue(Product.class);
                    if (product != null) {
                        fullProductList.add(product);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(error.getMessage());
            }
        });
    }

    // ================= SEARCH =================
    private void setupSearch() {

        if (searchBar == null) return;

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = s.toString().trim().toLowerCase();

                if (query.isEmpty()) {
                    productContainer.removeAllViews();
                    productContainer.setVisibility(View.GONE);
                    return;
                }

                productContainer.setVisibility(View.VISIBLE);
                searchFirebaseProducts(query);
            }

            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void searchFirebaseProducts(String query) {

        productContainer.removeAllViews();

        boolean found = false;

        for (Product product : fullProductList) {

            String name = product.getName();
            int price = product.getPrice();
            int imageResId = product.getImageResId();

            if (name == null) continue;

            if (name.toLowerCase().contains(query)) {

                found = true;

                LinearLayout row = new LinearLayout(MainMenu.this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setPadding(20, 20, 20, 20);

                TextView tv = new TextView(MainMenu.this);
                tv.setText(name + " - ₱" + price);
                tv.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                ));

                MaterialButton addBtn = new MaterialButton(MainMenu.this);
                addBtn.setText("Add");

                addBtn.setOnClickListener(v ->
                        addItemToCart(name, price, imageResId)
                );

                row.addView(tv);
                row.addView(addBtn);

                productContainer.addView(row);
            }
        }

        if (!found) {
            TextView empty = new TextView(MainMenu.this);
            empty.setText("No products found");
            empty.setPadding(20, 20, 20, 20);
            productContainer.addView(empty);
        }
    }

    // ================= DEFAULT PRODUCTS =================
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(error.getMessage());
            }
        });
    }

    private void addProductIfMissing(DataSnapshot snapshot, String name, int price, int stock, int imageResId) {
        if (!snapshot.hasChild(name)) {
            Product product = new Product(name, price, stock, imageResId);
            productsRef.child(name).setValue(product);
        }
    }

    // ================= CART =================
    private void addItemToCart(String name, int price, int imageResId) {

        for (CartItem item : cartList) {
            if (item.name != null && item.name.equals(name)) {
                item.quantity++;
                refreshCartInfo();
                showToast(name + " quantity increased");
                return;
            }
        }

        cartList.add(new CartItem(name, price, imageResId));
        refreshCartInfo();
        showToast(name + " added to cart");
    }

    private void refreshCartInfo() {

        int totalItems = 0;
        int totalPrice = 0;

        for (CartItem item : cartList) {
            totalItems += item.quantity;
            totalPrice += item.price * item.quantity;
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

    // ================= UI =================
    private void setupCartUI() {

        if (viewCartBtn != null) {
            viewCartBtn.setOnClickListener(v -> openCart());
        }

        backBtn = findViewById(R.id.backtostartbtn);
        if (backBtn != null) backBtn.setOnClickListener(v -> finish());

        cartBtn = findViewById(R.id.cartBtn);
        if (cartBtn != null) cartBtn.setOnClickListener(v -> openCart());
    }

    private void openCart() {
        Intent intent = new Intent(MainMenu.this, CartActivity.class);
        intent.putExtra("order_type", orderType); // FIXED SAFE PASSING
        startActivity(intent);
    }

    // ================= CATEGORY BUTTONS =================
    private void setupCategoryButtons() {
        silogBtn = findViewById(R.id.silogBtn);
        pastilBtn = findViewById(R.id.pastilBtn);
        shawarmaBtn = findViewById(R.id.shawarmaBtn);
        sizzlingBtn = findViewById(R.id.sizzlingBtn);
        ssdBtn = findViewById(R.id.ssdBtn);
        drinkBtn = findViewById(R.id.drinkBtn);

        if (silogBtn != null) silogBtn.setOnClickListener(v -> startActivity(new Intent(this, SilogActivity.class)));
        if (pastilBtn != null) pastilBtn.setOnClickListener(v -> startActivity(new Intent(this, PastilActivity.class)));
        if (shawarmaBtn != null) shawarmaBtn.setOnClickListener(v -> startActivity(new Intent(this, ShawarmaActivity.class)));
        if (sizzlingBtn != null) sizzlingBtn.setOnClickListener(v -> startActivity(new Intent(this, SizzlingActivity.class)));
        if (ssdBtn != null) ssdBtn.setOnClickListener(v -> startActivity(new Intent(this, SSDActivity.class)));
        if (drinkBtn != null) drinkBtn.setOnClickListener(v -> startActivity(new Intent(this, DrinksActivity.class)));
    }

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
        if (button != null)
            button.setOnClickListener(v -> addItemToCart(name, price, imageResId));
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