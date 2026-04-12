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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_shawarma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = shawarmas.get(position);

        holder.shawarmaName.setText(product.getName());
        holder.shawarmaPrice.setText("₱" + product.getPrice());
        holder.shawarmaImage.setImageResource(product.getImageResId());

        holder.addToCartBtn.setOnClickListener(v -> {

            CartItem cartItem = new CartItem(
                    product.getName(),
                    product.getPrice(),
                    1,
                    product.getImageResId()
            );

            MainMenu.cartList.add(cartItem);

            Toast.makeText(context,
                    product.getName() + " added to cart",
                    Toast.LENGTH_SHORT).show();

            if (listener != null) {
                listener.onCartUpdated();
            }
        });
    }

    @Override
    public int getItemCount() {
        return shawarmas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView shawarmaName, shawarmaPrice;
        MaterialButton addToCartBtn;
        ImageView shawarmaImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shawarmaName = itemView.findViewById(R.id.shawarmaName);
            shawarmaPrice = itemView.findViewById(R.id.shawarmaPrice); // ✅ important
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
            shawarmaImage = itemView.findViewById(R.id.shawarmaImage);
        }
    }
}