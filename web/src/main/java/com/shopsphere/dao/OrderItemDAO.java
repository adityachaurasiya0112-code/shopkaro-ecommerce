package com.shopsphere.dao;

import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for order_items table.
 */
public class OrderItemDAO {

    public List<OrderItem> getByOrderId(Connection conn, int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name, p.brand, p.image_url " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new OrderItem(
                            rs.getInt("order_item_id"),
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("brand"),
                            rs.getString("image_url"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    ));
                }
            }
        }
        return list;
    }

    public List<OrderItem> getByOrderId(int orderId) throws SQLException {
        try (Connection conn = DBUtil.get()) {
            return getByOrderId(conn, orderId);
        }
    }

    public void insert(Connection conn, int orderId, int productId, int quantity, double price) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.executeUpdate();
        }
    }
}
