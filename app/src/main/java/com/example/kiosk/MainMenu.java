package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        database = FirebaseDatabase.getInstance().getReference();
        productsRef = database.child("products");

        initializeDefaultProducts();

        cartPanel = findViewById(R.id.cartPanel);
        cartInfo = findViewById(R.id.cartInfo);
        viewCartBtn = findViewById(R.id.viewCartBtn);
        productContainer = findViewById(R.id.productContainer);
        searchBar = findViewById(R.id.searchBar);

        setupSearch(); // 🔥 ADDED SEARCH
        setupCartUI();
        setupCategoryButtons();
        setupAddToCartButtons();

        refreshCartInfo();
    }

    public void refreshCartUI() {
        refreshCartInfo();
    }
    // ================= SEARCH (FIREBASE LIVE) =================
    private void setupSearch() {

        if (searchBar == null) return;

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchFirebaseProducts(String query) {

        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productContainer.removeAllViews();

                boolean found = false;

                for (DataSnapshot ds : snapshot.getChildren()) {

                    String name = ds.child("name").getValue(String.class);
                    Integer price = ds.child("price").getValue(Integer.class);
                    Integer imageResId = ds.child("imageResId").getValue(Integer.class);

                    if (name == null || price == null || imageResId == null) continue;

                    if (name.toLowerCase().contains(query)) {

                        found = true;

                        View row = getLayoutInflater().inflate(
                                R.layout.item_search_product,
                                productContainer,
                                false
                        );

                        ImageView img = row.findViewById(R.id.searchImg);
                        TextView nameTxt = row.findViewById(R.id.searchName);
                        TextView priceTxt = row.findViewById(R.id.searchPrice);
                        MaterialButton addBtn = row.findViewById(R.id.searchAddBtn);

                        nameTxt.setText(name);
                        priceTxt.setText("₱" + price);
                        img.setImageResource(imageResId);

                        addBtn.setOnClickListener(v ->
                                addItemToCart(name, price, imageResId)
                        );

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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast(error.getMessage());
            }
        });
    }

    // ================= FIREBASE DEFAULT PRODUCTS =================
    private void initializeDefaultProducts() {

        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                addProductIfMissing(snapshot, "BaconSilog", 45, 10, R.drawable.baconsilog);
                addProductIfMissing(snapshot, "TapSilog", 45, 8, R.drawable.tapsilog);
                addProductIfMissing(snapshot, "ToSilog", 39, 5, R.drawable.tosilog);
                addProductIfMissing(snapshot, "BurgerSteak", 25, 6, R.drawable.burgersteak);
                addProductIfMissing(snapshot, "Shawarma", 25, 7, R.drawable.shawarmacute);
                addProductIfMissing(snapshot, "Siomai", 25, 12, R.drawable.siomairice);
                addProductIfMissing(snapshot, "BigSiomai", 40, 10, R.drawable.bigsiomairice);
                addProductIfMissing(snapshot, "Dumpling", 30, 8, R.drawable.dumplingrice);
                addProductIfMissing(snapshot, "JumboSiopao", 25, 15, R.drawable.jumbosiopao);
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
    public void addItemToCart(String name, int price, int imageResId) {

        boolean found = false;

        for (CartItem item : cartList) {
            if (item.name.equals(name)) {
                item.quantity++;
                found = true;
                break;
            }
        }

        if (!found) {
            cartList.add(new CartItem(name, price, imageResId, ""));
        }

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
        startActivity(intent);
    }

    // ================= CATEGORY =================
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

    // ================= ADD TO CART =================
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

        setAddToCartListener(addBaconBtn, "BaconSilog", 45, R.drawable.baconsilog);
        setAddToCartListener(addTapBtn, "TapSilog", 45, R.drawable.tapsilog);
        setAddToCartListener(addTosilogBtn, "ToSilog", 39, R.drawable.tosilog);
        setAddToCartListener(addBurgerBtn, "BurgerSteak", 25, R.drawable.burgersteak);
        setAddToCartListener(addShawarmaBtn, "Shawarma", 25, R.drawable.shawarmacute);
        setAddToCartListener(addSiomaiBtn, "Siomai", 25, R.drawable.siomairice);
        setAddToCartListener(addBigSiomaiBtn, "BigSiomai", 40, R.drawable.bigsiomairice);
        setAddToCartListener(addDumplingBtn, "Dumpling", 30, R.drawable.dumplingrice);
        setAddToCartListener(addSiopaoBtn, "JumboSiopao", 25, R.drawable.jumbosiopao);
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