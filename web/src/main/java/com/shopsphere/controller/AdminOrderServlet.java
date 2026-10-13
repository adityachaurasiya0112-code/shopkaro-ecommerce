package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.service.CheckoutService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Admin Order Management and Status Tracking.
 */
@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private final CheckoutService checkoutService = new CheckoutService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Order> orders = checkoutService.getAllOrders();
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error loading orders", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("updateStatus".equalsIgnoreCase(action)) {
                int orderId = Integer.parseInt(req.getParameter("orderId"));
                String status = req.getParameter("orderStatus");
                checkoutService.updateOrderStatus(orderId, status);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/orders");
        } catch (Exception e) {
            req.getSession().setAttribute("adminError", "Failed to update order: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/orders");
        }
    }
}
