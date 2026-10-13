package com.shopsphere.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderItemId;
    private int orderId;
    private int productId;
    private String productName;
    private String brand;
    private String imageUrl;
    private int quantity;
    private double price;

    public OrderItem() {}

    public OrderItem(int orderItemId, int orderId, int productId, String productName,
                     String brand, String imageUrl, int quantity, double price) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderItemId() { return orderItemId; }
    public int getId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getName() { return productName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getQty() { return quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getSubtotal() {
        return Math.round(price * quantity * 100.0) / 100.0;
    }
}
