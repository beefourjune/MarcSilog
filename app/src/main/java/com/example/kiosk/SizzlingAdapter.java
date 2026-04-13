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

public class SizzlingAdapter extends RecyclerView.Adapter<SizzlingAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> sizzlingList;
    private final OnCartUpdatedListener listener;

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }

    public SizzlingAdapter(Context context, List<Product> sizzlingList,
                           OnCartUpdatedListener listener) {
        this.context = context;
        this.sizzlingList = sizzlingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sizzling, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = sizzlingList.get(position);
        holder.sizzlingName.setText(product.getName());
        holder.addToCartBtn.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {
        boolean found = false;

        for (CartItem item : MainMenu.cartList) {
            if (item.name != null && item.name.equals(product.getName())) {
                item.quantity++;
                found = true;
                break;
            }
        }

        if (!found) {
            int imageId = product.getImageResId() != 0 ? product.getImageResId() : R.drawable.sisig;
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
        return sizzlingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sizzlingName;
        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sizzlingName = itemView.findViewById(R.id.sizzlingName);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}