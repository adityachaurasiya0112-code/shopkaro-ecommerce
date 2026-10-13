package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private int orderId;
    private int userId;
    private String customerName;
    private String customerEmail;
    private Integer addressId;
    private String addressText;
    private double totalAmount;
    private double discount;
    private String paymentMethod;
    private String paymentStatus;
    private String orderStatus;
    private Timestamp createdAt;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(int orderId, double totalAmount, String orderStatus, Timestamp createdAt) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public Order(int orderId, int userId, Integer addressId, double totalAmount,
                 double discount, String paymentMethod, String paymentStatus,
                 String orderStatus, Timestamp createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public int getOrderId() { return orderId; }
    public int getId() { return orderId; }
    public int id() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Integer getAddressId() { return addressId; }
    public void setAddressId(Integer addressId) { this.addressId = addressId; }

    public String getAddressText() { return addressText; }
    public void setAddressText(String addressText) { this.addressText = addressText; }

    public double getTotalAmount() { return totalAmount; }
    public double getTotal() { return totalAmount; }
    public double total() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getOrderStatus() { return orderStatus; }
    public String getStatus() { return orderStatus; }
    public String status() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp createdAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public int getItemsCount() {
        if (items == null) return 0;
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }
}
