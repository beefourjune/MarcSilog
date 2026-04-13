package com.example.kiosk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<Order> ordersList = new ArrayList<>();

    private DatabaseReference ordersRef;
    private ValueEventListener ordersListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView = view.findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new OrdersAdapter(ordersList);
        recyclerView.setAdapter(adapter);

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        loadOrdersFromFirebase();

        return view;
    }

    private void loadOrdersFromFirebase() {

        ordersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ordersList.clear();

                for (DataSnapshot orderSnap : snapshot.getChildren()) {

                    Order order = orderSnap.getValue(Order.class);
                    if (order == null) continue;

                    String key = orderSnap.getKey();

                    // 🔥 CRITICAL: Firebase key for buttons
                    order.setFirebaseKey(key);

                    // fallback display ID
                    if (order.getId() == null || order.getId().isEmpty()) {
                        order.setId(key);
                    }

                    // safe status
                    if (order.getStatus() == null) {
                        order.setStatus("pending");
                    }

                    Log.d("ORDERS_DEBUG", key + " | " + order.getStatus());

                    // ONLY pending
                    if ("pending".equals(order.getStatus())) {
                        ordersList.add(order);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ORDERS_ERROR", error.getMessage());
            }
        };

        ordersRef.addValueEventListener(ordersListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (ordersListener != null) {
            ordersRef.removeEventListener(ordersListener);
        }
    }
}