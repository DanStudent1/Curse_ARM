package com.example.curse_arm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnCartChangeListener {

    private RecyclerView recyclerView;
    private Button clearCartButton;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cartRecyclerView);
        clearCartButton = view.findViewById(R.id.clearCartButton);

        List<Product> cartItems = CartManager.getInstance().getCartItems();
        adapter = new CartAdapter(cartItems, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        clearCartButton.setOnClickListener(v -> {
            CartManager.getInstance().clearCart();
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    @Override
    public void onCartUpdated() {
        adapter.notifyDataSetChanged();  // обновляем корзину при изменении
    }
}
