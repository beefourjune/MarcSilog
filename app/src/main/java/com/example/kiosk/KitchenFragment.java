package com.example.kiosk;

import android.os.Bundle;
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

public class KitchenFragment extends Fragment {

    private RecyclerView recyclerView;
    private KitchenAdapter adapter;
    private List<Order> kitchenList;

    private DatabaseReference ordersRef;

    public KitchenFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // ✅ KEEPING YOUR ORIGINAL LAYOUT
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.menuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        kitchenList = new ArrayList<>();
        adapter = new KitchenAdapter(kitchenList);
        recyclerView.setAdapter(adapter);

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        loadKitchenOrders();

        return view;
    }

    // ================= LOAD KITCHEN ORDERS =================
    private void loadKitchenOrders() {

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                kitchenList.clear(); // ✅ prevents duplicates

                for (DataSnapshot snap : snapshot.getChildren()) {

                    Order order = snap.getValue(Order.class);

                    if (order != null) {

                        // ✅ VERY IMPORTANT (for Complete button later)
                        order.setFirebaseKey(snap.getKey());

                        // ✅ Ensure ID exists (for display)
                        if (order.getId() == null || order.getId().isEmpty()) {
                            order.setId(snap.getKey());
                        }

                        // ✅ Avoid null items crash
                        if (order.getItems() == null) {
                            order.setItems(new ArrayList<>());
                        }

                        // ✅ FILTER: ONLY preparing orders
                        if ("preparing".equals(order.getStatus())) {
                            kitchenList.add(order);
                        }
                    }
                }

                adapter.notifyDataSetChanged(); // ✅ refresh UI
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // optional error handling
            }
        });
    }
}