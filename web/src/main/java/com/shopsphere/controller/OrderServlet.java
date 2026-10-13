package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import com.shopsphere.service.CheckoutService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Customer Orders and Order Tracking.
 */
@WebServlet(urlPatterns = {"/orders", "/order-details"})
public class OrderServlet extends HttpServlet {

    private final CheckoutService checkoutService = new CheckoutService();

    private int getUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return ((User) session.getAttribute("user")).getUserId();
        }
        return -1;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = getUserId(req);
        if (userId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();
        String idParam = req.getParameter("id");

        try {
            if ("/order-details".equals(path) || (idParam != null && !idParam.isBlank())) {
                int orderId = Integer.parseInt(idParam);
                Order order = checkoutService.getOrder(orderId);

                HttpSession session = req.getSession();
                User currentUser = (User) session.getAttribute("user");

                // Security check: order must belong to user or user must be admin
                if (order == null || (order.getUserId() != userId && !currentUser.isAdmin())) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: You cannot view this order.");
                    return;
                }

                req.setAttribute("order", order);
                req.getRequestDispatcher("/order-details.jsp").forward(req, resp);
            } else {
                List<Order> orders = checkoutService.orders(userId);
                req.setAttribute("orders", orders);
                req.getRequestDispatcher("/orders.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/orders");
        } catch (SQLException e) {
            throw new ServletException("Error querying orders", e);
        }
    }
}
