package com.shopsphere.service;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Service Layer: Order Management and Tracking.
 * Directly matches RTU syllabus requirement: OrderService -> OrderDAO -> OrderItemDAO.
 */
public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();

    public int placeOrder(int userId, Integer addressId, String couponCode, String paymentMethod) throws SQLException {
        return orderDAO.placeOrder(userId, addressId, couponCode, paymentMethod);
    }

    public int placeOrder(int userId) throws SQLException {
        return orderDAO.place(userId);
    }

    public List<Order> getOrdersByUser(int userId) throws SQLException {
        return orderDAO.list(userId);
    }

    public Order getOrderDetails(int orderId) throws SQLException {
        return orderDAO.getById(orderId);
    }

    public List<Order> getAllOrdersForAdmin() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public boolean updateStatus(int orderId, String newStatus) throws SQLException {
        return orderDAO.updateStatus(orderId, newStatus);
    }

    public Map<String, Object> getDashboardStats() throws SQLException {
        return orderDAO.getDashboardStats();
    }
}
