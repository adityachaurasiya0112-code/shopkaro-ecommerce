package com.shopsphere.controller;

import com.shopsphere.model.Address;
import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import com.shopsphere.service.UserService;
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
 * Controller for User Profile and Address Book management.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();
    private final AuthService authService = new AuthService();

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

        try {
            User user = userService.getUser(userId);
            List<Address> addresses = userService.getUserAddresses(userId);

            req.setAttribute("profileUser", user);
            req.setAttribute("addresses", addresses);

            req.getRequestDispatcher("/profile.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error loading profile", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = getUserId(req);
        if (userId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");

        try {
            if ("addAddress".equalsIgnoreCase(action)) {
                String line = req.getParameter("addressLine");
                String city = req.getParameter("city");
                String state = req.getParameter("state");
                String pincode = req.getParameter("pincode");
                String type = req.getParameter("addressType");
                userService.addAddress(userId, line, city, state, pincode, type);
                req.getSession().setAttribute("profileSuccess", "New address added successfully.");
            } else if ("deleteAddress".equalsIgnoreCase(action)) {
                int addressId = Integer.parseInt(req.getParameter("addressId"));
                userService.deleteAddress(addressId, userId);
                req.getSession().setAttribute("profileSuccess", "Address removed.");
            } else if ("changePassword".equalsIgnoreCase(action)) {
                String newPassword = req.getParameter("newPassword");
                String confirmPassword = req.getParameter("confirmPassword");
                if (newPassword != null && newPassword.equals(confirmPassword)) {
                    authService.changePassword(userId, newPassword);
                    req.getSession().setAttribute("profileSuccess", "Password updated successfully.");
                } else {
                    req.getSession().setAttribute("profileError", "Passwords do not match.");
                }
            }
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (Exception e) {
            req.getSession().setAttribute("profileError", "Action failed: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }
}
