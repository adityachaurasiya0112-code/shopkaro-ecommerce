package com.shopsphere.dao;

import com.shopsphere.model.Payment;
import com.shopsphere.util.DBUtil;

import java.sql.*;

/**
 * Data Access Object for payments table.
 */
public class PaymentDAO {

    public Payment getByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Payment(
                            rs.getInt("payment_id"),
                            rs.getInt("order_id"),
                            rs.getString("payment_method"),
                            rs.getString("transaction_reference"),
                            rs.getDouble("amount"),
                            rs.getString("payment_status"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    public int insert(Connection conn, int orderId, String method, String txRef, double amount, String status) throws SQLException {
        String sql = "INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orderId);
            ps.setString(2, method);
            ps.setString(3, txRef);
            ps.setDouble(4, amount);
            ps.setString(5, status);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
}
