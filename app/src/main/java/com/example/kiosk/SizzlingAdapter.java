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

        CartItem cartItem = new CartItem(
                product.getName(),
                product.getPrice(),
                R.drawable.sisig, // or product image if you have it
                1
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
                    });
        }
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