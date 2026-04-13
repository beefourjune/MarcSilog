package com.example.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ShawarmaAdapter extends RecyclerView.Adapter<ShawarmaAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> shawarmas;
    private final OnCartUpdatedListener listener;

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }

    public ShawarmaAdapter(Context context, List<Product> shawarmas, OnCartUpdatedListener listener) {
        this.context = context;
        this.shawarmas = shawarmas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shawarma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = shawarmas.get(position);
        holder.shawarmaName.setText(product.getName());
        holder.addToCartBtn.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {
        boolean found = false;

        // Check for duplicates in the local cart list
        for (CartItem item : MainMenu.cartList) {
            if (item.name != null && item.name.equals(product.getName())) {
                item.quantity++;
                found = true;
                break;
            }
        }

        if (!found) {
            // Use fixed image resource for Shawarma if product doesn't have one
            int imageId = product.getImageResId() != 0 ? product.getImageResId() : R.drawable.shawarmacute;
            MainMenu.cartList.add(new CartItem(product.getName(), product.getPrice(), imageId));
        }

        if (listener != null) {
            listener.onCartUpdated();
        }

        String message = found ? 
            product.getName() + " quantity updated" : 
            product.getName() + " added to cart";

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return shawarmas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shawarmaName;
        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shawarmaName = itemView.findViewById(R.id.shawarmaName);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}