package com.shopsphere.dao;

import com.shopsphere.model.*;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object for orders table.
 * Demonstrates JDBC Transaction Management (commit/rollback) and atomic inventory updates.
 * Satisfies RTU security rule: Order totals and discounts are calculated strictly on the server.
 */
public class OrderDAO {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final CouponDAO couponDAO = new CouponDAO();

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        try { o.setCustomerName(rs.getString("customer_name")); } catch (SQLException ignored) {}
        try { o.setCustomerEmail(rs.getString("customer_email")); } catch (SQLException ignored) {}
        int addrId = rs.getInt("address_id");
        o.setAddressId(rs.wasNull() ? null : addrId);
        try { o.setAddressText(rs.getString("address_text")); } catch (SQLException ignored) {}
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setDiscount(rs.getDouble("discount"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }

    /**
     * Places an order inside a single JDBC Transaction.
     */
    public int placeOrder(int userId, Integer addressId, String couponCode, String paymentMethod) throws SQLException {
        try (Connection conn = DBUtil.get()) {
            conn.setAutoCommit(false);
            try {
                // 1. Fetch current cart items from DB
                List<CartItem> cartItems = cartDAO.list(conn, userId);
                if (cartItems.isEmpty()) {
                    throw new SQLException("Your shopping cart is empty.");
                }

                // 2. Calculate subtotal strictly on server from DB items
                double subtotal = 0.0;
                for (CartItem item : cartItems) {
                    subtotal += item.getSubtotal();
                }

                // 3. Validate coupon if provided
                double discount = 0.0;
                Coupon coupon = null;
                if (couponCode != null && !couponCode.trim().isEmpty()) {
                    coupon = couponDAO.getByCode(couponCode.trim());
                    if (coupon != null && coupon.isValidForOrder(subtotal)) {
                        discount = coupon.calculateDiscount(subtotal);
                    }
                }

                double finalTotal = Math.max(0.0, subtotal - discount);
                finalTotal = Math.round(finalTotal * 100.0) / 100.0;

                // 4. Insert order
                int orderId;
                String insertOrderSql = "INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, payment_status, order_status) " +
                                        "VALUES (?, ?, ?, ?, ?, 'PAID', 'PLACED')";
                try (PreparedStatement ps = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId);
                    if (addressId != null && addressId > 0) {
                        ps.setInt(2, addressId);
                    } else {
                        ps.setNull(2, Types.INTEGER);
                    }
                    ps.setDouble(3, finalTotal);
                    ps.setDouble(4, discount);
                    ps.setString(5, (paymentMethod == null || paymentMethod.isBlank()) ? "MOCK" : paymentMethod);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            orderId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to generate order ID.");
                        }
                    }
                }

                // 5. Insert order items & atomically deduct stock
                for (CartItem item : cartItems) {
                    // Atomic stock deduction: WHERE stock >= quantity
                    String deductStockSql = "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";
                    try (PreparedStatement ps = conn.prepareStatement(deductStockSql)) {
                        ps.setInt(1, item.getQuantity());
                        ps.setInt(2, item.getProductId());
                        ps.setInt(3, item.getQuantity());
                        int updated = ps.executeUpdate();
                        if (updated == 0) {
                            throw new SQLException("Insufficient stock for product: " + item.getName());
                        }
                    }

                    // Insert into order_items
                    orderItemDAO.insert(conn, orderId, item.getProductId(), item.getQuantity(), item.getFinalPrice());
                }

                // 6. Record payment
                String txRef = "TXN-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 9000 + 1000);
                paymentDAO.insert(conn, orderId, (paymentMethod == null ? "MOCK" : paymentMethod), txRef, finalTotal, "PAID");

                // 7. Record coupon usage if applied
                if (coupon != null && discount > 0) {
                    couponDAO.recordUsage(conn, coupon.getCouponId(), userId, orderId);
                }

                // 8. Clear user cart
                cartDAO.clear(conn, userId);

                // Commit transaction
                conn.commit();
                return orderId;
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public int place(int userId) throws SQLException {
        return placeOrder(userId, null, null, "MOCK");
    }

    public List<Order> list(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS customer_name, u.email AS customer_email, " +
                     "CONCAT_WS(', ', a.address_line, a.city, a.state, a.pincode) AS address_text " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.user_id = ? ORDER BY o.order_id DESC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(orderItemDAO.getByOrderId(order.getOrderId()));
                    list.add(order);
                }
            }
        }
        return list;
    }

    public Order getById(int orderId) throws SQLException {
        String sql = "SELECT o.*, u.name AS customer_name, u.email AS customer_email, " +
                     "CONCAT_WS(', ', a.address_line, a.city, a.state, a.pincode) AS address_text " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "WHERE o.order_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(orderItemDAO.getByOrderId(order.getOrderId()));
                    return order;
                }
            }
        }
        return null;
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS customer_name, u.email AS customer_email, " +
                     "CONCAT_WS(', ', a.address_line, a.city, a.state, a.pincode) AS address_text " +
                     "FROM orders o " +
                     "JOIN users u ON o.user_id = u.user_id " +
                     "LEFT JOIN addresses a ON o.address_id = a.address_id " +
                     "ORDER BY o.order_id DESC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(orderItemDAO.getByOrderId(order.getOrderId()));
                list.add(order);
            }
        }
        return list;
    }

    public boolean updateStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        try (Connection conn = DBUtil.get()) {
            // Total Revenue
            try (PreparedStatement ps = conn.prepareStatement("SELECT COALESCE(SUM(total_amount), 0) FROM orders WHERE payment_status = 'PAID'");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.put("totalRevenue", rs.getDouble(1));
            }
            // Total Orders
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM orders");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.put("totalOrders", rs.getInt(1));
            }
            // Total Customers
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE role = 'CUSTOMER'");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.put("totalCustomers", rs.getInt(1));
            }
            // Low Stock Products count (threshold <= 10)
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE status = 1 AND stock <= 10");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) stats.put("lowStockCount", rs.getInt(1));
            }
        }
        return stats;
    }
}
