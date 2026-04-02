package com.example.kiosk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Order> orders = new ArrayList<>();

    public OrdersAdapter(List<Order> orders) {
        if (orders != null) this.orders = orders;
    }

    // ✅ New method to update the list dynamically
    public void setOrders(List<Order> orders) {
        if (orders != null) {
            this.orders = orders;
            notifyDataSetChanged();
        }
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
        String status = order.isCompleted() ? "Completed" : "Pending";
        String kitchen = order.isInKitchen() ? "In Kitchen" : "Not in Kitchen";
        holder.orderText.setText("Order ID: " + order.getId() + "\nStatus: " + status + "\n" + kitchen);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderText;

        ViewHolder(View itemView) {
            super(itemView);
            orderText = itemView.findViewById(R.id.orderText);
        }
    }
}

