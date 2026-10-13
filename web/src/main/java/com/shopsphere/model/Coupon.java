package com.shopsphere.model;

import java.io.Serializable;
import java.sql.Date;

public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;

    private int couponId;
    private String code;
    private String discountType; // PERCENT or FLAT
    private double discountValue;
    private double minimumOrder;
    private double maximumDiscount;
    private Date expiryDate;
    private int status;

    public Coupon() {}

    public Coupon(int couponId, String code, String discountType, double discountValue,
                  double minimumOrder, double maximumDiscount, Date expiryDate, int status) {
        this.couponId = couponId;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minimumOrder = minimumOrder;
        this.maximumDiscount = maximumDiscount;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    public int getCouponId() { return couponId; }
    public int getId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }

    public double getMinimumOrder() { return minimumOrder; }
    public void setMinimumOrder(double minimumOrder) { this.minimumOrder = minimumOrder; }

    public double getMaximumDiscount() { return maximumDiscount; }
    public void setMaximumDiscount(double maximumDiscount) { this.maximumDiscount = maximumDiscount; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public boolean isValidForOrder(double orderTotal) {
        if (status != 1) return false;
        if (orderTotal < minimumOrder) return false;
        if (expiryDate != null && expiryDate.before(new java.util.Date())) return false;
        return true;
    }

    public double calculateDiscount(double orderTotal) {
        if (!isValidForOrder(orderTotal)) return 0.0;
        double discount;
        if ("PERCENT".equalsIgnoreCase(discountType)) {
            discount = orderTotal * (discountValue / 100.0);
            if (maximumDiscount > 0 && discount > maximumDiscount) {
                discount = maximumDiscount;
            }
        } else {
            discount = discountValue;
        }
        return Math.min(discount, orderTotal);
    }
}
