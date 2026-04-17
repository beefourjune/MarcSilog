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
        TextView name, price, quantityText;
        Button addBtn, minusBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgAddOn);
            name = itemView.findViewById(R.id.txtName);
            price = itemView.findViewById(R.id.txtPrice);
            addBtn = itemView.findViewById(R.id.btnAdd);
            minusBtn = itemView.findViewById(R.id.btnMinus);
            quantityText = itemView.findViewById(R.id.txtQuantity);
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
        holder.price.setText(context.getString(R.string.price_format, item.getPrice()));
        holder.img.setImageResource(item.getImageResId());

        // Find matching item in global cart to show current quantity
        CartItem cartItem = null;
        for (CartItem c : MainMenu.cartList) {
            if (c.getName().equals(item.getName())) {
                cartItem = c;
                break;
            }
        }
        
        int currentQty = (cartItem != null) ? cartItem.getQuantity() : 0;
        holder.quantityText.setText(String.valueOf(currentQty));

        holder.addBtn.setOnClickListener(v -> {
            boolean found = false;
            for (CartItem c : MainMenu.cartList) {
                if (c.getName().equals(item.getName())) {
                    c.setQuantity(c.getQuantity() + 1);
                    found = true;
                    break;
                }
            }

            if (!found) {
                MainMenu.cartList.add(new CartItem(item.getName(), item.getPrice(), item.getImageResId(), 1));
            }

            notifyItemChanged(position);
            if (listener != null) listener.onCartChanged();
        });

        holder.minusBtn.setOnClickListener(v -> {
            for (int i = 0; i < MainMenu.cartList.size(); i++) {
                CartItem c = MainMenu.cartList.get(i);
                if (c.getName().equals(item.getName())) {
                    if (c.getQuantity() > 1) {
                        c.setQuantity(c.getQuantity() - 1);
                    } else {
                        MainMenu.cartList.remove(i);
                    }
                    break;
                }
            }
            notifyItemChanged(position);
            if (listener != null) listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}