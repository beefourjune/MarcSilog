package com.example.kiosk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private List<Dish> dishList;

    // Constructor
    public DishAdapter(List<Dish> dishList) {
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.nameText.setText(dish.getName());
        holder.priceText.setText("₱" + dish.getPrice());
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public void updateList(List<Dish> newList) {
        this.dishList = newList;
        notifyDataSetChanged();
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, priceText;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.dishNameText);
            priceText = itemView.findViewById(R.id.dishPriceText);
        }
    }
}