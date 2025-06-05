package com.example.curse_arm;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private final List<Product> productList = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FloatingActionButton fabAdd, fabScanQr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);

        fabAdd = view.findViewById(R.id.fabAddProduct);
        fabScanQr = view.findViewById(R.id.fabScanQR);

        fabAdd.setOnClickListener(v -> showAddProductDialog());

        fabScanQr.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QRScannerActivity.class);
            startActivity(intent);
        });

        loadProductsFromFirestore();

        return view;
    }

    private void loadProductsFromFirestore() {
        db.collection("products")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    productList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Product product = doc.toObject(Product.class);
                        productList.add(product);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Ошибка загрузки товаров", Toast.LENGTH_SHORT).show();
                });
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Добавить товар");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 30, 40, 10);

        EditText inputName = new EditText(getContext());
        inputName.setHint("Название");
        layout.addView(inputName);

        EditText inputDescription = new EditText(getContext());
        inputDescription.setHint("Описание");
        layout.addView(inputDescription);

        EditText inputPrice = new EditText(getContext());
        inputPrice.setHint("Цена");
        inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputPrice);

        builder.setView(layout);

        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            String description = inputDescription.getText().toString().trim();
            String priceStr = inputPrice.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            Map<String, Object> newProduct = new HashMap<>();
            newProduct.put("name", name);
            newProduct.put("description", description);
            newProduct.put("price", price);

            db.collection("products").add(newProduct)
                    .addOnSuccessListener(docRef -> {
                        Toast.makeText(getContext(), "Товар добавлен", Toast.LENGTH_SHORT).show();
                        loadProductsFromFirestore();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Ошибка добавления товара", Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
