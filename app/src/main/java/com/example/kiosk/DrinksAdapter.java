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

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> drinksList;
    private final OnCartUpdatedListener listener;

    public interface OnCartUpdatedListener {
        void onCartUpdated();
    }

    public DrinksAdapter(Context context, List<Product> drinksList,
                         OnCartUpdatedListener listener) {
        this.context = context;
        this.drinksList = drinksList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_drinks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = drinksList.get(position);
        holder.drinkName.setText(product.getName());
        holder.addToCartBtn.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {
        boolean found = false;
        for (CartItem item : MainMenu.cartList) {
            if (item.getName() != null && item.getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            MainMenu.cartList.add(new CartItem(product.getName(), product.getPrice(), product.getImageResId()));
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
        return drinksList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView drinkName;
        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkName = itemView.findViewById(R.id.drinkName);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}