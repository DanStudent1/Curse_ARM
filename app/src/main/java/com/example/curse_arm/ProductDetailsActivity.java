package com.example.curse_arm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView nameView, descriptionView, priceView;
    ImageView imageView;
    Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        nameView = findViewById(R.id.detailProductName);
        descriptionView = findViewById(R.id.detailProductDescription);
        priceView = findViewById(R.id.detailProductPrice);
        imageView = findViewById(R.id.detailProductImage);
        addToCartButton = findViewById(R.id.addToCartButton);

        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0);
        String imageUrl = getIntent().getStringExtra("image");

        nameView.setText(name);
        descriptionView.setText(description);
        priceView.setText("Цена: " + price + " ₽");

        // Загрузка изображения через Glide
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // Заглушка, если нет картинки
                .error(R.drawable.placeholder)   // Заглушка, если ошибка
                .into(imageView);

        addToCartButton.setOnClickListener(v -> {
            Product product = new Product(name, description, price, imageUrl);
            CartManager.getInstance().addProduct(product);
            Toast.makeText(this, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
        });
    }
}
