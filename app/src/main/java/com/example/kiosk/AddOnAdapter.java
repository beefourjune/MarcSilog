package com.example.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddOnAdapter extends RecyclerView.Adapter<AddOnAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> list;
    private OnCartChanged listener;

    public interface OnCartChanged {
        void onCartChanged();
    }
    public AddOnAdapter(Context context, List<CartItem> list, OnCartChanged listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price;
        Button addBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgAddOn);
            name = itemView.findViewById(R.id.txtName);
            price = itemView.findViewById(R.id.txtPrice);
            addBtn = itemView.findViewById(R.id.btnAdd);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_addon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartItem item = list.get(position);

        holder.name.setText(item.getName());
        holder.price.setText("₱" + item.getPrice());
        holder.img.setImageResource(item.getImageResId());

        holder.addBtn.setOnClickListener(v -> {

            boolean exists = false;

            for (CartItem c : MainMenu.cartList) {
                if (c.getName().equals(item.getName())) {
                    c.setQuantity(c.getQuantity() + 1);
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                MainMenu.cartList.add(new CartItem(
                        item.getName(),
                        item.getPrice(),
                        item.getImageResId(),
                        1
                ));
            }

            if (listener != null) {
                listener.onCartChanged();
            }

            Toast.makeText(context, item.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}