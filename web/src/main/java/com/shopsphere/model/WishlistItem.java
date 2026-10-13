package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class WishlistItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int wishlistId;
    private int userId;
    private int productId;
    private String name;
    private String brand;
    private double price;
    private double discount;
    private double finalPrice;
    private String imageUrl;
    private int stock;
    private Timestamp createdAt;

    public WishlistItem() {}

    public WishlistItem(int wishlistId, int userId, int productId, String name, String brand,
                        double price, double discount, double finalPrice, String imageUrl,
                        int stock, Timestamp createdAt) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    public int getWishlistId() { return wishlistId; }
    public void setWishlistId(int wishlistId) { this.wishlistId = wishlistId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(double finalPrice) { this.finalPrice = finalPrice; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
