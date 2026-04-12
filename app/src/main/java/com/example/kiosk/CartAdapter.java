package com.example.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final Context context;
    private final List<CartItem> cartItems;
    private final CartListener listener;

    public interface CartListener {
        void onQuantityChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartItem item = cartItems.get(position);

        holder.itemName.setText(item.getName());
        holder.itemPrice.setText("₱" + item.getPrice());
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));

        // ================= IMAGE SAFE LOAD =================
        try {
            if (item.getImageResId() > 0) {
                holder.itemImage.setImageResource(item.getImageResId());
            } else {
                holder.itemImage.setImageResource(R.drawable.baconsilog);
            }
        } catch (Exception e) {
            holder.itemImage.setImageResource(R.drawable.baconsilog);
        }

        // ================= INCREASE =================
        holder.btnIncrease.setOnClickListener(v -> {
            item.increaseQuantity();
            holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
            listener.onQuantityChanged();
        });

        // ================= DECREASE =================
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.decreaseQuantity();
                holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
                listener.onQuantityChanged();
            }
        });

        // ================= REMOVE =================
        holder.btnRemove.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) {

                cartItems.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, cartItems.size());

                listener.onQuantityChanged();

                Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // ================= VIEW HOLDER =================
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQuantity;
        ImageView itemImage;
        Button btnIncrease, btnDecrease;
        ImageButton btnRemove;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemImage = itemView.findViewById(R.id.itemImage);

            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}