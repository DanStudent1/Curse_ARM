package com.example.curse_arm;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Product> cartItems = new ArrayList<>();

    private CartManager() {}

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<Product> getCartItems() {
        return new ArrayList<>(cartItems); // чтобы избежать прямого изменения списка
    }

    public void addProduct(Product product) {
        for (Product item : cartItems) {
            if (item.getName().equals(product.getName())) {
                item.increaseQuantity();
                return;
            }
        }
        cartItems.add(product);
    }

    public void removeProduct(Product product) {
        cartItems.remove(product);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void updateQuantity(Product product, int newQuantity) {
        for (Product item : cartItems) {
            if (item.getName().equals(product.getName())) {
                item.setQuantity(newQuantity);
                return;
            }
        }
    }
}
