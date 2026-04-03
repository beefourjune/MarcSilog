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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // Convert Product into CartItem
        CartItem cartItem = new CartItem(
                product.getName(),
                product.getPrice(),
                1,
                product.getStock()
        );

        // Add to local cart list
        MainMenu.cartList.add(cartItem);

        // Add to Firebase cart
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart");
        String cartItemKey = cartRef.push().getKey();

        if (cartItemKey != null) {
            cartRef.child(cartItemKey).setValue(cartItem)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(
                                context,
                                product.getName() + " added to cart",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Update floating cart panel
                        if (listener != null) {
                            listener.onCartUpdated();
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(
                                    context,
                                    "Failed to add to cart",
                                    Toast.LENGTH_SHORT
                            ).show()
                    );
        }
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