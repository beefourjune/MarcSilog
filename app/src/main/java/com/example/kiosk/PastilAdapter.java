package com.example.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView; // ✅ added
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PastilAdapter extends RecyclerView.Adapter<PastilAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> pastils;
    private final CartUpdateListener cartUpdateListener;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public PastilAdapter(Context context, List<Product> pastils, CartUpdateListener listener) {
        this.context = context;
        this.pastils = pastils;
        this.cartUpdateListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pastil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = pastils.get(position);
        holder.pastilName.setText(product.getName());
        holder.pastilPrice.setText("₱" + product.getPrice());

        // ✅ added (this is the key fix)
        holder.pastilImage.setImageResource(product.getImageResId());

        holder.addToCartBtn.setOnClickListener(v -> {
            MainMenu.cartList.add(new CartItem(product.getName(), product.getPrice(), product.getImageResId()));
            if (cartUpdateListener != null) cartUpdateListener.onCartUpdated();
            Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return pastils.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView pastilName, pastilPrice;
        MaterialButton addToCartBtn;
        ImageView pastilImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pastilName = itemView.findViewById(R.id.pastilName);
            pastilPrice = itemView.findViewById(R.id.pastilPrice); // ✅ important
            pastilImage = itemView.findViewById(R.id.pastilImage);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}