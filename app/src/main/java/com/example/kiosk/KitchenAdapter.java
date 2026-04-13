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
        // 🔥 Using item_menu.xml as per your project structure for Kitchen
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        String orderId = order.getId();
        if (orderId == null || orderId.isEmpty()) {
            orderId = "UNKNOWN";
        }

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

        holder.orderText.setText("Order ID: " + orderId + "\n\nItems:\n" + itemsText);

        // ✅ CHANGE BUTTON TO COMPLETE
        holder.btnAction.setText("Complete");
        holder.btnAction.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF4CAF50)); // Green

        holder.btnAction.setOnClickListener(v -> {
            String firebaseKey = order.getFirebaseKey();
            if (firebaseKey == null || firebaseKey.isEmpty()) {
                Toast.makeText(v.getContext(), "Invalid Firebase Key", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("orders")
                    .child(firebaseKey);

            ref.child("status").setValue("completed")
                    .addOnSuccessListener(unused ->
                            Toast.makeText(v.getContext(), "Order Completed", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(v.getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
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
            // 🔥 Match IDs in item_menu.xml
            orderText = itemView.findViewById(R.id.menuText);
            btnAction = itemView.findViewById(R.id.prepareBtn);
        }
    }
}