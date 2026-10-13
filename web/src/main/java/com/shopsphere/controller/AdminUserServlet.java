package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Admin User Management.
 */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> users = userService.getAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/admin/users.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error loading users", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("toggleStatus".equalsIgnoreCase(action)) {
                int userId = Integer.parseInt(req.getParameter("userId"));
                int newStatus = Integer.parseInt(req.getParameter("status"));
                userService.toggleUserStatus(userId, newStatus);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/users");
        } catch (Exception e) {
            req.getSession().setAttribute("adminError", "Failed to update user status: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/users");
        }
    }
}
