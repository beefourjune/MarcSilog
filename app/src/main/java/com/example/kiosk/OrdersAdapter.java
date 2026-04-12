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

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Order> orders = new ArrayList<>();

    public OrdersAdapter(List<Order> orders) {
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

        // ✅ Order ID display
        String displayId = order.getId() != null ? order.getId() : "UNKNOWN";

        // ✅ Firebase key fallback safety
        String key = order.getFirebaseKey();
        if (key == null || key.isEmpty()) {
            key = displayId;
        }

        final String firebaseKey = key;

        // ✅ Build items text
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

        // ✅ Display order details
        holder.orderText.setText(
                "Order ID: " + displayId + "\n\nItems:\n" + itemsText
        );

        // ✅ BUTTON: Send to Kitchen (PREPARE)
        holder.btnPrepare.setOnClickListener(v -> {

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("orders")
                    .child(firebaseKey);

            ref.child("status").setValue("preparing")
                    .addOnSuccessListener(unused ->
                            Toast.makeText(v.getContext(),
                                    "Sent to Kitchen",
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

    // ================= VIEW HOLDER =================
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderText;
        Button btnPrepare;

        ViewHolder(View itemView) {
            super(itemView);

            orderText = itemView.findViewById(R.id.orderText);
            btnPrepare = itemView.findViewById(R.id.prepareBtn);
        }
    }
}