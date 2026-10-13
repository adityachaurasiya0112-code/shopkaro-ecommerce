package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for cart table.
 */
public class CartDAO {

    private CartItem mapCartItem(ResultSet rs) throws SQLException {
        double price = rs.getDouble("price");
        double discount = rs.getDouble("discount");
        double finalPrice = discount > 0 ? (Math.round(price * (100.0 - discount)) / 100.0) : price;

        return new CartItem(
                rs.getInt("cart_id"),
                rs.getInt("user_id"),
                rs.getInt("product_id"),
                rs.getString("name"),
                rs.getString("brand"),
                price,
                discount,
                finalPrice,
                rs.getInt("quantity"),
                rs.getString("image_url"),
                rs.getInt("stock")
        );
    }

    public List<CartItem> list(Connection conn, int userId) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.cart_id, c.user_id, c.product_id, c.quantity, " +
                     "p.name, p.brand, p.price, p.discount, p.image_url, p.stock " +
                     "FROM cart c JOIN products p ON c.product_id = p.product_id " +
                     "WHERE c.user_id = ? AND p.status = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCartItem(rs));
                }
            }
        }
        return list;
    }

    public List<CartItem> list(int userId) throws SQLException {
        try (Connection conn = DBUtil.get()) {
            return list(conn, userId);
        }
    }

    public void add(int userId, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity <= 0 ? 1 : quantity);
            ps.executeUpdate();
        }
    }

    public void add(int userId, int productId) throws SQLException {
        add(userId, productId, 1);
    }

    public void updateQuantity(int userId, int productId, int quantity) throws SQLException {
        if (quantity <= 0) {
            remove(userId, productId);
            return;
        }
        String sql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
    }

    public void remove(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    public void clear(Connection conn, int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public void clear(int userId) throws SQLException {
        try (Connection conn = DBUtil.get()) {
            clear(conn, userId);
        }
    }

    public int getCount(int userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart WHERE user_id = ?";
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
