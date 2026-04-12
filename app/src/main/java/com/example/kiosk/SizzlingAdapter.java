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
        holder.sizzlingPrice.setText("₱" + product.getPrice());
        holder.sizzlingImage.setImageResource(product.getImageResId());

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
        return sizzlingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView sizzlingName, sizzlingPrice;
        ImageView sizzlingImage;
        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sizzlingName = itemView.findViewById(R.id.sizzlingName);
            sizzlingPrice = itemView.findViewById(R.id.sizzlingPrice);
            sizzlingImage = itemView.findViewById(R.id.sizzlingImage);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}