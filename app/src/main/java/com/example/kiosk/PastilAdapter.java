package com.example.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        holder.pastilDesc.setText(product.getDescription());
        holder.pastilPrice.setText("₱" + product.getPrice());
        holder.pastilImage.setImageResource(product.getImageResId());

        holder.addToCartBtn.setOnClickListener(v -> {

            FlavorFragment flavorFragment = new FlavorFragment();

            flavorFragment.setFlavorListener(flavor -> {

                String itemName = product.getName() + " (" + flavor + ")";
                String itemKey = product.getName() + "_" + flavor;



                boolean found = false;

                for (CartItem item : MainMenu.cartList) {
                    if (item.key != null && item.key.equals(itemKey)) {
                        item.quantity++;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    CartItem newItem = new CartItem(
                            itemName,
                            product.getPrice(),
                            product.getImageResId(),
                            product.getDescription()
                    );

                    newItem.key = itemKey; // ✅ IMPORTANT
                    MainMenu.cartList.add(newItem);
                }

                if (cartUpdateListener != null)
                    cartUpdateListener.onCartUpdated();

                Toast.makeText(context,
                        itemName + " added to cart",
                        Toast.LENGTH_SHORT).show();
            });

            flavorFragment.show(fragmentManager, "flavor");
        });



    }

    @Override
    public int getItemCount() {
        return pastils.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pastilName, pastilPrice, pastilDesc;

        ImageView pastilImage;

        MaterialButton addToCartBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pastilName = itemView.findViewById(R.id.pastilName);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
            pastilImage = itemView.findViewById(R.id.pastilImage);
            pastilPrice = itemView.findViewById(R.id.pastilPrice);
            pastilDesc = itemView.findViewById(R.id.pastilDesc);
        }
    }



}
