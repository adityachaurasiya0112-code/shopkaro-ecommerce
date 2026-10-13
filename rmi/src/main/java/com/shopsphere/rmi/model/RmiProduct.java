package com.shopsphere.rmi.model;

import java.io.Serializable;

/**
 * Serializable Product entity transmitted over RMI.
 */
public class RmiProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int productId;
    private final String name;
    private final String brand;
    private final double price;
    private final int stock;

    public RmiProduct(int productId, String name, String brand, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    @Override
    public String toString() {
        return "RmiProduct{#" + productId + ", " + name + " (" + brand + "), ₹" + price + ", Stock=" + stock + "}";
    }
}
