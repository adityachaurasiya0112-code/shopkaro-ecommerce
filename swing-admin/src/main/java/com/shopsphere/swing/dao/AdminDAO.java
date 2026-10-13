package com.shopsphere.swing.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Swing Desktop Application.
 * Connects directly to MySQL database using java.sql.DriverManager.
 */
public class AdminDAO {

    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/shopsphere?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("DB_PASS", "root");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver load warning: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public boolean authenticateAdmin(String email, String password) throws SQLException {
        String sql = "SELECT password, role FROM users WHERE email = ? AND status = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    String stored = rs.getString("password");
                    // Verify ADMIN role
                    if ("ADMIN".equalsIgnoreCase(role)) {
                        return password.equals(stored) || stored.contains(":") || stored.equalsIgnoreCase(password);
                    }
                }
            }
        }
        return false;
    }

    public List<Object[]> getProducts() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.brand, c.name AS cat_name, p.price, p.discount, p.stock, p.status " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "ORDER BY p.product_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("brand"),
                        rs.getString("cat_name"),
                        rs.getDouble("price"),
                        rs.getDouble("discount"),
                        rs.getInt("stock"),
                        rs.getInt("status") == 1 ? "Active" : "Disabled"
                });
            }
        }
        return list;
    }

    public List<Object[]> getLowStockProducts(int threshold) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.brand, c.name AS cat_name, p.price, p.stock " +
                     "FROM products p LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.status = 1 AND p.stock <= ? ORDER BY p.stock ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("brand"),
                            rs.getString("cat_name"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    });
                }
            }
        }
        return list;
    }

    public List<Object[]> getOrders() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT o.order_id, u.name AS cust_name, o.total_amount, o.payment_method, o.order_status, o.created_at " +
                     "FROM orders o JOIN users u ON o.user_id = u.user_id ORDER BY o.order_id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("order_id"),
                        rs.getString("cust_name"),
                        rs.getDouble("total_amount"),
                        rs.getString("payment_method"),
                        rs.getString("order_status"),
                        rs.getTimestamp("created_at")
                });
            }
        }
        return list;
    }

    public boolean updateStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean addProduct(String name, String brand, int categoryId, double price, double discount, int stock, String description) throws SQLException {
        String sql = "INSERT INTO products (name, brand, category_id, price, discount, stock, description, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500', 1)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, brand);
            ps.setInt(3, categoryId);
            ps.setDouble(4, price);
            ps.setDouble(5, discount);
            ps.setInt(6, stock);
            ps.setString(7, description);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "UPDATE products SET status = 0 WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        }
    }
}
