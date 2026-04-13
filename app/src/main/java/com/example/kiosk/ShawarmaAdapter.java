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
        View view = LayoutInflater.from(context).inflate(R.layout.item_shawarma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = shawarmas.get(position);
        holder.shawarmaDesc.setText(product.getDescription());
        holder.shawarmaName.setText(product.getName());
        holder.shawarmaPrice.setText("₱" + product.getPrice());
        holder.shawarmaImage.setImageResource(product.getImageResId());

        holder.addToCartBtn.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {

        if (context instanceof MainMenu) {
            ((MainMenu) context).addItemToCart(
                    product.getName(),
                    product.getPrice(),
                    product.getImageResId()
            );
        }

        if (listener != null) {
            listener.onCartUpdated();
        }

        Toast.makeText(context,
                product.getName() + " added to cart",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return shawarmas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView shawarmaImage;
        TextView shawarmaName, shawarmaDesc;
        TextView shawarmaPrice;
        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            shawarmaDesc = itemView.findViewById(R.id.shawarmaDesc);
            shawarmaImage = itemView.findViewById(R.id.shawarmaImage);
            shawarmaName = itemView.findViewById(R.id.shawarmaName);
            shawarmaPrice = itemView.findViewById(R.id.shawarmaPrice);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}