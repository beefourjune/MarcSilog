package com.example.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SSDAdapter extends RecyclerView.Adapter<SSDAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> ssdList;
    private final OnCartUpdatedListener listener;

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }

    public SSDAdapter(Context context, List<Product> ssdList,
                      OnCartUpdatedListener listener) {
        this.context = context;
        this.ssdList = ssdList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_ssd, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = ssdList.get(position);
        holder.ssdName.setText(product.getName());
        holder.ssdImage.setImageResource(product.getImageResId() != 0 ? product.getImageResId() : R.drawable.jumbosiopao);
        holder.ssdPrice.setText(context.getString(R.string.price_format, product.getPrice()));
        holder.addToCartBtn.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {
        boolean found = false;

        // Group duplicates in SSD category
        for (CartItem item : MainMenu.cartList) {
            if (item.name != null && item.name.equals(product.getName())) {
                item.quantity++;
                found = true;
                break;
            }
        }

        if (!found) {
            int imageId = product.getImageResId() != 0 ? product.getImageResId() : R.drawable.jumbosiopao;
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
        return ssdList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ssdName, ssdPrice;
        MaterialButton addToCartBtn;
        ImageView ssdImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ssdName = itemView.findViewById(R.id.ssdName);
            ssdPrice = itemView.findViewById(R.id.ssdPrice);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
            ssdImage = itemView.findViewById(R.id.ssdImage);
        }
    }
}