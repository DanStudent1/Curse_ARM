package com.example.curse_arm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Product> cartItems;
    private final OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartUpdated();
    }

    public CartAdapter(List<Product> cartItems, OnCartChangeListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.nameText.setText(product.getName());
        holder.priceText.setText("₽ " + product.getPrice());
        holder.quantityText.setText(String.valueOf(product.getQuantity()));

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .error(R.drawable.placeholder)
                .into(holder.imageView);

        holder.increaseButton.setOnClickListener(v -> {
            product.increaseQuantity();
            notifyItemChanged(position);
            listener.onCartUpdated();
        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                product.decreaseQuantity();
                notifyItemChanged(position);
            } else {
                cartItems.remove(position);
                notifyItemRemoved(position);
            }
            listener.onCartUpdated();
        });

        holder.removeButton.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, priceText, quantityText;
        ImageView imageView;
        Button increaseButton, decreaseButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textCartName);
            priceText = itemView.findViewById(R.id.textCartPrice);
            quantityText = itemView.findViewById(R.id.textCartQuantity);
            imageView = itemView.findViewById(R.id.cartProductImage);
            increaseButton = itemView.findViewById(R.id.buttonIncrease);
            decreaseButton = itemView.findViewById(R.id.buttonDecrease);
            removeButton = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
