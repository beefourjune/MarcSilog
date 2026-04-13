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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        holder.drinkDesc.setText(product.getDescription());
        holder.drinkName.setText(product.getName());
        holder.drinkPrice.setText("₱" + product.getPrice());
        holder.drinkImage.setImageResource(product.getImageResId());


        holder.addToCartBtn.setOnClickListener(v -> addToCart(product));
    }

    private void addToCart(Product product) {

        CartItem cartItem = new CartItem(
                product.getName(),
                product.getPrice(),
                R.drawable.water, // or product image if you have it
                1,
                product.getDescription()
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
        return drinksList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView drinkName, drinkPrice, drinkDesc;

        ImageView drinkImage;
        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkDesc = itemView.findViewById(R.id.drinkDesc);
            drinkPrice = itemView.findViewById(R.id.drinkPrice);
            drinkName = itemView.findViewById(R.id.drinkName);
            drinkImage = itemView.findViewById(R.id.drinkImage);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}
