package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for coupons and coupon_usage tables.
 */
public class CouponDAO {

    private Coupon mapCoupon(ResultSet rs) throws SQLException {
        return new Coupon(
                rs.getInt("coupon_id"),
                rs.getString("code"),
                rs.getString("discount_type"),
                rs.getDouble("discount_value"),
                rs.getDouble("minimum_order"),
                rs.getDouble("maximum_discount"),
                rs.getDate("expiry_date"),
                rs.getInt("status")
        );
    }

    public Coupon getByCode(String code) throws SQLException {
        if (code == null || code.trim().isEmpty()) return null;
        String sql = "SELECT * FROM coupons WHERE UPPER(code) = UPPER(?) AND status = 1";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCoupon(rs);
            }
        }
        return null;
    }

    public List<Coupon> getAll() throws SQLException {
        List<Coupon> list = new ArrayList<>();
        String sql = "SELECT * FROM coupons ORDER BY coupon_id DESC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCoupon(rs));
            }
        }
        return list;
    }

    public int insert(Coupon c) throws SQLException {
        String sql = "INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getCode().toUpperCase().trim());
            ps.setString(2, c.getDiscountType() == null ? "PERCENT" : c.getDiscountType());
            ps.setDouble(3, c.getDiscountValue());
            ps.setDouble(4, c.getMinimumOrder());
            ps.setDouble(5, c.getMaximumDiscount());
            ps.setDate(6, c.getExpiryDate());
            ps.setInt(7, c.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public boolean recordUsage(Connection conn, int couponId, int userId, int orderId) throws SQLException {
        String sql = "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean isUsedByUser(int couponId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM coupon_usage WHERE coupon_id = ? AND user_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
