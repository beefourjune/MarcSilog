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

        holder.addToCartBtn.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {

        CartItem cartItem = new CartItem(
                product.getName(),
                product.getPrice(),
                1,
                product.getStock()
        );

        MainMenu.cartList.add(cartItem);

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
        return ssdList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView ssdName;
        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ssdName = itemView.findViewById(R.id.ssdName);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}
