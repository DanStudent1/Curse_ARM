package com.example.curse_arm;

import java.util.List;

public class Order {
    private String userName;
    private String timestamp;
    private String status;
    private List<OrderItem> items; // данные из Firebase (productId + quantity)
    private List<OrderDisplayItem> displayItems; // отображаемые данные о товарах

    public Order() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public List<OrderDisplayItem> getDisplayItems() {
        return displayItems;
    }

    public void setDisplayItems(List<OrderDisplayItem> displayItems) {
        this.displayItems = displayItems;
    }
}
