package com.example.curse_arm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private final List<Order> orderList = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public OrdersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView = view.findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);

        fetchOrdersFromFirebase();

        return view;
    }

    private void fetchOrdersFromFirebase() {
        CollectionReference ordersRef = db.collection("orders");
        ordersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            orderList.clear();

            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Order order = doc.toObject(Order.class);
                List<OrderItem> rawItems = order.getItems();

                if (rawItems == null || rawItems.isEmpty()) {
                    order.setDisplayItems(new ArrayList<>());
                    orderList.add(order);
                    continue;
                }

                List<OrderDisplayItem> displayItems = new ArrayList<>();
                order.setDisplayItems(displayItems); // чтобы избежать null

                // Загрузка продуктов по каждому productId
                for (OrderItem item : rawItems) {
                    db.collection("products").document(item.getProductId())
                            .get()
                            .addOnSuccessListener(productDoc -> {
                                if (productDoc.exists()) {
                                    Product product = productDoc.toObject(Product.class);

                                    OrderDisplayItem displayItem = new OrderDisplayItem();
                                    displayItem.setName(product.getName());
                                    displayItem.setPrice(product.getPrice());
                                    displayItem.setImageUrl(product.getImageUrl());
                                    displayItem.setQuantity(item.getQuantity());

                                    displayItems.add(displayItem);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                }

                orderList.add(order);
            }

            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Ошибка загрузки заказов", Toast.LENGTH_SHORT).show();
        });
    }
}
