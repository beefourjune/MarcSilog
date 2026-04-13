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
                itemsText.append("• ")
                        .append(item.name)
                        .append(" x")
                        .append(item.quantity)
                        .append("\n");
            }
        } else {
            itemsText.append("No items");
        }

        String type = order.getOrderType() != null ? order.getOrderType() : "TAKE OUT";

        holder.orderText.setText(
                "Order ID: " + displayId +
                        " (" + type + ")\n" +
                        itemsText
        );

        holder.btnAction.setText("Serve");

        holder.btnAction.setOnClickListener(v -> {

            String firebaseKey = order.getFirebaseKey();
            if (firebaseKey == null) return;

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("orders")
                    .child(firebaseKey);

            ref.child("status").setValue("served")
                    .addOnSuccessListener(unused ->
                            Toast.makeText(v.getContext(),
                                    "Order Served",
                                    Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(v.getContext(),
                                    "Error: " + e.getMessage(),
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