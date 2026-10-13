package com.shopsphere.controller;

import com.shopsphere.model.Order;
import com.shopsphere.model.Product;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Controller for Admin Dashboard.
 * Protected by AdminFilter (/admin/*).
 */
@WebServlet(urlPatterns = {"/admin/dashboard", "/admin"})
public class AdminDashboardServlet extends HttpServlet {

    private final CheckoutService checkoutService = new CheckoutService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Map<String, Object> stats = checkoutService.getDashboardStats();
            List<Order> recentOrders = checkoutService.getAllOrders();
            if (recentOrders.size() > 5) {
                recentOrders = recentOrders.subList(0, 5);
            }
            List<Product> lowStock = productService.getLowStock(15);

            req.setAttribute("stats", stats);
            req.setAttribute("recentOrders", recentOrders);
            req.setAttribute("lowStockProducts", lowStock);

            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Unable to load admin dashboard", e);
        }
    }
}
