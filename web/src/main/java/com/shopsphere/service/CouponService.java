package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;

import java.sql.SQLException;
import java.util.List;

/**
 * Service Layer: Coupon validation and administration.
 */
public class CouponService {

    private final CouponDAO dao = new CouponDAO();

    public Coupon validateCoupon(String code, double orderTotal) throws SQLException {
        if (code == null || code.isBlank()) return null;
        Coupon c = dao.getByCode(code.trim());
        if (c != null && c.isValidForOrder(orderTotal)) {
            return c;
        }
        return null;
    }

    public List<Coupon> getAllCoupons() throws SQLException {
        return dao.getAll();
    }

    public int addCoupon(Coupon coupon) throws SQLException {
        if (coupon.getCode() == null || coupon.getCode().isBlank()) {
            throw new IllegalArgumentException("Coupon code is required.");
        }
        if (coupon.getDiscountValue() <= 0) {
            throw new IllegalArgumentException("Discount value must be greater than 0.");
        }
        return dao.insert(coupon);
    }
}
