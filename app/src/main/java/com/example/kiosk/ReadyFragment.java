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

public class ReadyFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReadyAdapter adapter;
    private List<Order> readyList = new ArrayList<>();

    private DatabaseReference ordersRef;
    private ValueEventListener readyListener;

    public ReadyFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        // Reusing the same layout as OrdersFragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView = view.findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ReadyAdapter(readyList);
        recyclerView.setAdapter(adapter);

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        loadReadyOrders();

        return view;
    }

    private void loadReadyOrders() {

        readyListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                readyList.clear();

                for (DataSnapshot orderSnap : snapshot.getChildren()) {

                    Order order = orderSnap.getValue(Order.class);
                    if (order == null) continue;

                    order.setFirebaseKey(orderSnap.getKey());

                    if (order.getId() == null || order.getId().isEmpty()) {
                        order.setId(orderSnap.getKey());
                    }

                    // FILTER: ONLY ready orders move to Ready tab
                    if ("ready".equals(order.getStatus())) {
                        readyList.add(order);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("READY_ERROR", error.getMessage());
            }
        };

        ordersRef.addValueEventListener(readyListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (readyListener != null) {
            ordersRef.removeEventListener(readyListener);
        }
    }
}