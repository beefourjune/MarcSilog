package com.example.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddOnAdapter extends RecyclerView.Adapter<AddOnAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> list;
    private OnCartChanged listener;

    public interface OnCartChanged {
        void onCartChanged();
    }
    public AddOnAdapter(Context context, List<CartItem> list, OnCartChanged listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        Button btnAdd, btnMinus;
        TextView txtQuantity;
        TextView name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            img = itemView.findViewById(R.id.imgAddOn);
            name = itemView.findViewById(R.id.txtName);
            price = itemView.findViewById(R.id.txtPrice);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_addon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartItem item = list.get(position);

        holder.name.setText(item.getName());
        holder.price.setText("₱" + item.getPrice());
        holder.img.setImageResource(item.getImageResId());

        holder.txtQuantity.setText(String.valueOf(item.quantity));

        holder.btnAdd.setOnClickListener(v -> {

            item.quantity++;

            MainMenu.cartList.add(
                    new CartItem(
                            item.name,
                            item.price,
                            item.imageResId,
                            item.quantity,
                            item.description
                    )
            );

            holder.txtQuantity.setText(String.valueOf(item.quantity));
        });

        holder.btnMinus.setOnClickListener(v -> {

            if(item.quantity > 0){
                item.quantity--;
                holder.txtQuantity.setText(String.valueOf(item.quantity));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}