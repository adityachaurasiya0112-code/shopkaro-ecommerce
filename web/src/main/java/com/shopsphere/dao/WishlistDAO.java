package com.shopsphere.dao;

import com.shopsphere.model.WishlistItem;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for wishlist table.
 */
public class WishlistDAO {

    public List<WishlistItem> list(int userId) throws SQLException {
        List<WishlistItem> list = new ArrayList<>();
        String sql = "SELECT w.wishlist_id, w.user_id, w.product_id, w.created_at, " +
                     "p.name, p.brand, p.price, p.discount, p.image_url, p.stock " +
                     "FROM wishlist w JOIN products p ON w.product_id = p.product_id " +
                     "WHERE w.user_id = ? AND p.status = 1 ORDER BY w.wishlist_id DESC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double price = rs.getDouble("price");
                    double discount = rs.getDouble("discount");
                    double finalPrice = discount > 0 ? (Math.round(price * (100.0 - discount)) / 100.0) : price;

                    list.add(new WishlistItem(
                            rs.getInt("wishlist_id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("brand"),
                            price,
                            discount,
                            finalPrice,
                            rs.getString("image_url"),
                            rs.getInt("stock"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return list;
    }

    public boolean add(int userId, int productId) throws SQLException {
        String sql = "INSERT IGNORE INTO wishlist (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean remove(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean exists(int userId, int productId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int getCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
}
