package com.example.curse_arm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.userName.setText(order.getUserName());
        holder.timestamp.setText(order.getTimestamp());
        holder.status.setText("Статус: " + order.getStatus());

        // Очищаем контейнер перед добавлением новых карточек товаров
        holder.itemsContainer.removeAllViews();

        // 👇 Проверка на null
        if (order.getDisplayItems() != null) {
            for (OrderDisplayItem item : order.getDisplayItems()) {
                View itemView = LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.item_order_product, holder.itemsContainer, false);

                ImageView image = itemView.findViewById(R.id.productImage);
                TextView name = itemView.findViewById(R.id.productName);
                TextView price = itemView.findViewById(R.id.productPrice);
                TextView quantity = itemView.findViewById(R.id.productQuantity);

                Glide.with(itemView.getContext())
                        .load(item.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(image);

                name.setText(item.getName());
                price.setText("₽ " + item.getPrice());
                quantity.setText("x" + item.getQuantity());

                holder.itemsContainer.addView(itemView);
            }
        } else {
            // если нет данных
            TextView noItemsText = new TextView(holder.itemView.getContext());
            noItemsText.setText("Нет данных о товарах");
            holder.itemsContainer.addView(noItemsText);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView userName, timestamp, status;
        LinearLayout itemsContainer;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.orderUser);
            timestamp = itemView.findViewById(R.id.orderTimestamp);
            status = itemView.findViewById(R.id.orderStatus);
            itemsContainer = itemView.findViewById(R.id.orderItemsContainer);
        }
    }
}
