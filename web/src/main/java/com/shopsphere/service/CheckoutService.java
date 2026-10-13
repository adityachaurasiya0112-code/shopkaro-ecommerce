package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Service Layer: Shopping Cart, Checkout, and Order Management.
 */
public class CheckoutService {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public List<CartItem> cart(int userId) throws SQLException {
        return cartDAO.list(userId);
    }

    public void add(int userId, int productId) throws SQLException {
        cartDAO.add(userId, productId, 1);
    }

    public void add(int userId, int productId, int quantity) throws SQLException {
        cartDAO.add(userId, productId, quantity);
    }

    public void updateQuantity(int userId, int productId, int quantity) throws SQLException {
        cartDAO.updateQuantity(userId, productId, quantity);
    }

    public void remove(int userId, int productId) throws SQLException {
        cartDAO.remove(userId, productId);
    }

    public void clear(int userId) throws SQLException {
        cartDAO.clear(userId);
    }

    public int getCartCount(int userId) throws SQLException {
        return cartDAO.getCount(userId);
    }

    public int placeOrder(int userId, Integer addressId, String couponCode, String paymentMethod) throws SQLException {
        return orderDAO.placeOrder(userId, addressId, couponCode, paymentMethod);
    }

    public int place(int userId) throws SQLException {
        return orderDAO.place(userId);
    }

    public List<Order> orders(int userId) throws SQLException {
        return orderDAO.list(userId);
    }

    public Order getOrder(int orderId) throws SQLException {
        return orderDAO.getById(orderId);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        return orderDAO.updateStatus(orderId, status);
    }

    public Map<String, Object> getDashboardStats() throws SQLException {
        return orderDAO.getDashboardStats();
    }
}
