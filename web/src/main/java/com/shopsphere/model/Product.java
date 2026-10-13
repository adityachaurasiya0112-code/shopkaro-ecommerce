package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private int categoryId;
    private String categoryName;
    private String name;
    private String brand;
    private String description;
    private double price;
    private double discount;
    private int stock;
    private String imageUrl;
    private boolean status = true;
    private Timestamp createdAt;

    public Product() {}

    public Product(int productId, int categoryId, String name, String brand, String description,
                   double price, double discount, int stock, String imageUrl, boolean status) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public int getProductId() { return productId; }
    public int getId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public double getFinalPrice() {
        if (discount > 0) {
            double discounted = price * (1.0 - (discount / 100.0));
            return Math.round(discounted * 100.0) / 100.0;
        }
        return price;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + productId +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}
