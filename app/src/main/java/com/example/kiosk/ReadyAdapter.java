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

public class ReadyAdapter extends RecyclerView.Adapter<ReadyAdapter.ViewHolder> {

    private List<Order> orders = new ArrayList<>();

    public ReadyAdapter(List<Order> orders) {
        if (orders != null) this.orders = orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using item_menu.xml which has the "Serve" button text defined by user
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        String displayId = order.getId() != null ? order.getId() : "UNKNOWN";
        StringBuilder itemsText = new StringBuilder();

        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (CartItem item : order.getItems()) {
                itemsText.append("• ").append(item.name).append(" x").append(item.quantity).append("\n");
            }
        } else {
            itemsText.append("No items");
        }

        holder.orderText.setText("Order ID: " + displayId + "\n" + itemsText);

        // ✅ CHANGE BUTTON TO "SERVE" or "READY" as requested
        // The user mentioned "ready button" but also "Serve" is in the XML.
        // I will set it to "Serve" to match the final action that makes it "gone".
        holder.btnAction.setText("Serve");
        holder.btnAction.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF4CAF50)); // Green

        holder.btnAction.setOnClickListener(v -> {
            String firebaseKey = order.getFirebaseKey();
            if (firebaseKey == null) return;

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("orders")
                    .child(firebaseKey);

            // Set status to "served" so it's no longer "pending", "preparing", or "completed"
            // This will make it disappear from all dashboard fragments
            ref.child("status").setValue("served")
                    .addOnSuccessListener(unused ->
                            Toast.makeText(v.getContext(), "Order Served", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
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