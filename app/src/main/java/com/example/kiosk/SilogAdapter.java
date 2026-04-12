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

public class SilogAdapter extends RecyclerView.Adapter<SilogAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> silogs;
    private final CartUpdateListener cartUpdateListener;

    // Listener interface to notify activity when cart changes
    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public SilogAdapter(Context context, List<Product> silogs, CartUpdateListener listener) {
        this.context = context;
        this.silogs = silogs;
        this.cartUpdateListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_silog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = silogs.get(position);
        holder.silogName.setText(product.getName());


        // ✅ added (image binding)
        holder.silogImage.setImageResource(product.getImageResId());
        holder.silogPrice.setText("₱" + product.getPrice());

        holder.addToCartBtn.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {
        // Add product to static MainMenu cart list
        MainMenu.cartList.add(new CartItem(product.getName(), product.getPrice(), product.getImageResId()));

        // Notify activity to update floating panel
        if (cartUpdateListener != null) {
            cartUpdateListener.onCartUpdated();
        }

        // Toast confirmation
        Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return silogs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView silogName, silogPrice;
        MaterialButton addToCartBtn;
        ImageView silogImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            silogName = itemView.findViewById(R.id.silogName);
            silogPrice = itemView.findViewById(R.id.silogPrice); // ✅ FIXED
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
            silogImage = itemView.findViewById(R.id.silogImage);
        }
    }
}