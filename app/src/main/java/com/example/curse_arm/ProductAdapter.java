package com.example.curse_arm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList;

    public ProductAdapter(List<Product> products) {
        this.productList = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameText.setText(product.getName());
        holder.descriptionText.setText(product.getDescription());
        holder.priceText.setText("₽ " + product.getPrice());

        // Загрузка изображения
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        // Обработка нажатия на кнопку "Добавить в корзину"
        holder.addToCartButton.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product);
            Toast.makeText(holder.itemView.getContext(), "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, descriptionText, priceText;
        ImageView imageView;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textName);
            descriptionText = itemView.findViewById(R.id.textDescription);
            priceText = itemView.findViewById(R.id.textPrice);
            imageView = itemView.findViewById(R.id.imageProduct);
            addToCartButton = itemView.findViewById(R.id.buttonAddToCart);
        }
    }
}
