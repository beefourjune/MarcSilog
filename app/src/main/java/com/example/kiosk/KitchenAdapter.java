package com.example.kiosk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.ViewHolder> {

    private List<Order> orders = new ArrayList<>();

    public KitchenAdapter(List<Order> orders) {
        if (orders != null) this.orders = orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Order order = orders.get(position);

        // ✅ FIX: use Firebase-safe ID only
        String orderId = order.getId();

        if (orderId == null || orderId.isEmpty()) {
            orderId = "UNKNOWN";
        }

        // 🔥 BUILD ITEMS TEXT
        StringBuilder itemsText = new StringBuilder();

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (CartItem item : order.getItems()) {
                itemsText.append("• ")
                        .append(item.name)
                        .append(" x")
                        .append(item.quantity)
                        .append(" - ₱")
                        .append(item.price * item.quantity)
                        .append("\n");
            }
        } else {
            itemsText.append("No items");
        }

        holder.orderText.setText(
                "Order ID: " + orderId + "\n\nItems:\n" + itemsText
        );

        // ✔ BUTTON TEXT
        holder.btnAction.setText("Prepare");

        // ✔ BUTTON ACTION (FIXED + SAFE)
        holder.btnAction.setOnClickListener(v -> {

            if (order.getId() == null) {
                Toast.makeText(v.getContext(),
                        "Invalid Order ID",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("orders")
                    .child(order.getId());

            ref.child("status").setValue("preparing")
                    .addOnSuccessListener(unused ->
                            Toast.makeText(v.getContext(),
                                    "Order set to Preparing",
                                    Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(v.getContext(),
                                    "Failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show()
                    );
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderText;
        Button btnAction;

        ViewHolder(View itemView) {
            super(itemView);

            orderText = itemView.findViewById(R.id.menuText);
            btnAction = itemView.findViewById(R.id.prepareBtn);
        }
    }
}