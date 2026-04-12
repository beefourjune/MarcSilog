package com.example.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PastilAdapter extends RecyclerView.Adapter<PastilAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> pastils;
    private final CartUpdateListener cartUpdateListener;

    private FragmentManager fragmentManager;


    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public PastilAdapter(Context context, List<Product> pastils, CartUpdateListener listener, FragmentManager fragmentManager) {
        this.context = context;
        this.pastils = pastils;
        this.cartUpdateListener = listener;
        this.fragmentManager = fragmentManager;
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

        holder.addToCartBtn.setOnClickListener(v -> {

            FlavorFragment flavorFragment = new FlavorFragment();

            flavorFragment.setFlavorListener(flavor -> {

                MainMenu.cartList.add(
                        new CartItem(
                                product.getName() + " (" + flavor + ")",
                                product.getPrice(),
                                product.getImageResId()
                        )
                );

                if (cartUpdateListener != null)
                    cartUpdateListener.onCartUpdated();

                Toast.makeText(context,
                        product.getName() + " - " + flavor + " added to cart",
                        Toast.LENGTH_SHORT).show();
            });

            flavorFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "flavor");
        });



    }

    @Override
    public int getItemCount() {
        return pastils.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pastilName;
        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pastilName = itemView.findViewById(R.id.pastilName);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }



}
