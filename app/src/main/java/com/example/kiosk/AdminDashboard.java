package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends AppCompatActivity {

    private MaterialButton backToStartBtn;
    private EditText searchBar;
    private TextView pendingNumber, kitchenNumber;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private DatabaseReference rootRef;
    private DatabaseReference ordersRef;
    private DatabaseReference productsRef;

    private OrdersAdapter ordersAdapter;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        backToStartBtn = findViewById(R.id.backtostartbtn);
        searchBar = findViewById(R.id.searchBar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        pendingNumber = findViewById(R.id.pendingNumber);
        kitchenNumber = findViewById(R.id.kitchenNumber);

        rootRef = FirebaseDatabase.getInstance().getReference();
        ordersRef = rootRef.child("orders");
        productsRef = rootRef.child("products");

        ordersAdapter = new OrdersAdapter(new ArrayList<>());
        productsAdapter = new ProductsAdapter(new ArrayList<>());

        backToStartBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        loadOrders();
        loadProducts();

        // ✔ TAB SETUP
        String[] tabs = {"Orders", "Kitchen", "Ready"};

        viewPager.setOffscreenPageLimit(3);

        AdminPagerAdapter adapter =
                new AdminPagerAdapter(AdminDashboard.this, tabs.length);

        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabs[position])
        ).attach();
    }

    // ================= ORDERS =================
    private void loadOrders() {

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Order> orderList = new ArrayList<>();
                int pending = 0;
                int kitchen = 0;

                for (DataSnapshot snap : snapshot.getChildren()) {

                    Order order = snap.getValue(Order.class);
                    if (order == null) continue;

                    order.setFirebaseKey(snap.getKey());

                    if (order.getId() == null) {
                        order.setId(snap.getKey().substring(0,
                                Math.min(4, snap.getKey().length())));
                    }

                    if (order.getItems() == null) {
                        order.setItems(new ArrayList<>());
                    }

                    orderList.add(order);

                    // ✔ NEW SYSTEM (STATUS-BASED)
                    String status = order.getStatus();

                    if ("pending".equals(status)) {
                        pending++;
                    }

                    if ("preparing".equals(status)) {
                        kitchen++;
                    }
                }

                pendingNumber.setText(String.valueOf(pending));
                kitchenNumber.setText(String.valueOf(kitchen));

                ordersAdapter.setOrders(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboard.this,
                        "Failed to load orders: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================= PRODUCTS =================
    private void loadProducts() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Product> productList = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {

                    Product product = snap.getValue(Product.class);

                    if (product != null) {
                        productList.add(product);
                    }
                }

                productsAdapter.setProducts(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboard.this,
                        "Failed to load products: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================= SAVE PRODUCT =================
    private void addOrUpdateProduct(String name, int price, int stock) {

        Product product = new Product(name, price, stock);

        productsRef.child(name).setValue(product)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}