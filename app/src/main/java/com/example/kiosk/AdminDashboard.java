package com.example.kiosk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
    private TextView pendingOrdersText, kitchenQueueText, pendingNumber, kitchenNumber;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    // --- Firebase references ---
    private DatabaseReference database;
    private DatabaseReference productsRef;
    private DatabaseReference ordersRef;
    private DatabaseReference adminsRef; // optional for admin login

    // --- RecyclerView adapters ---
    private OrdersAdapter ordersAdapter;
    private ProductsAdapter productsAdapter;

    // --- RecyclerViews ---
    private RecyclerView ordersRecyclerView;
    private RecyclerView productsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        // --- Initialize views ---
        backToStartBtn = findViewById(R.id.backtostartbtn);
        searchBar = findViewById(R.id.searchBar);
        pendingOrdersText = findViewById(R.id.pendingOrdersText);
        kitchenQueueText = findViewById(R.id.kitchenQueueText);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        pendingNumber = findViewById(R.id.pendingNumber);
        kitchenNumber = findViewById(R.id.kitchenNumber);

        // Optional: find RecyclerViews if inside tabs
        // ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        // productsRecyclerView = findViewById(R.id.productsRecyclerView);

        // --- Firebase init ---
        database = FirebaseDatabase.getInstance().getReference();
        productsRef = database.child("products");
        ordersRef = database.child("orders");
        adminsRef = database.child("admins"); // optional for managing admins

        // --- Initialize adapters ---
        ordersAdapter = new OrdersAdapter(new ArrayList<>());
        productsAdapter = new ProductsAdapter(new ArrayList<>());

        // --- Back button / logout ---
        backToStartBtn.setOnClickListener(v -> {
            Toast.makeText(AdminDashboard.this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminDashboard.this, LoginActivity.class));
            finish();
        });

        // --- Load orders and products dynamically ---
        loadOrders();
        loadProducts();

        // --- Setup tabs + viewpager ---
        String[] tabTitles = {"Orders", "Kitchen", "Ready"};
        AdminPagerAdapter adapter = new AdminPagerAdapter(this, tabTitles.length);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
    }

    // --- Load orders dynamically ---
    private void loadOrders() {
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Order> orderList = new ArrayList<>();
                int pendingOrders = 0;
                int kitchenQueue = 0;

                for (DataSnapshot orderSnap : snapshot.getChildren()) {
                    Order order = orderSnap.getValue(Order.class);
                    if (order != null) {
                        orderList.add(order);
                        if (!order.isCompleted()) pendingOrders++;
                        if (order.isInKitchen()) kitchenQueue++;
                    }
                }

                pendingNumber.setText(String.valueOf(pendingOrders));
                kitchenNumber.setText(String.valueOf(kitchenQueue));

                // Pass orderList to RecyclerView Adapter
                ordersAdapter.setOrders(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboard.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Load products dynamically ---
    private void loadProducts() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> productList = new ArrayList<>();
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    Product product = productSnap.getValue(Product.class);
                    if (product != null) productList.add(product);
                }
                // Pass productList to RecyclerView Adapter
                productsAdapter.setProducts(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboard.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Helper method to add or update a product ---
    private void addOrUpdateProduct(String name, int price, int stock) {
        Product product = new Product(name, price, stock);
        productsRef.child(name).setValue(product)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Product saved", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save product", Toast.LENGTH_SHORT).show()
                );
    }
}