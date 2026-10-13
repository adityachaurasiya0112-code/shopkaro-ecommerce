package com.shopsphere.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int cartId;
    private int userId;
    private int productId;
    private String name;
    private String brand;
    private double price;
    private double discount;
    private double finalPrice;
    private int quantity;
    private String imageUrl;
    private int stock;

    public CartItem() {}

    public CartItem(int cartId, int userId, int productId, String name, String brand,
                    double price, double discount, double finalPrice, int quantity,
                    String imageUrl, int stock) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int productId() { return productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String name() { return name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double price() { return price; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getQty() { return quantity; }
    public int qty() { return quantity; }

    public double getSubtotal() {
        return Math.round(finalPrice * quantity * 100.0) / 100.0;
    }
    public double subtotal() { return getSubtotal(); }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
