package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for Customer Registration.
 * Demonstrates RTU syllabus: Server-side validation and secure password creation.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String mobile = req.getParameter("mobile");

        if (password == null || !password.equals(confirmPassword)) {
            req.setAttribute("error", "Passwords do not match.");
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("mobile", mobile);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        try {
            User newUser = authService.register(name, email, password, mobile);

            // Auto-login upon successful registration
            HttpSession session = req.getSession(true);
            session.setAttribute("user", newUser);
            session.setAttribute("role", newUser.getRole());

            resp.sendRedirect(req.getContextPath() + "/products?welcome=true");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("mobile", mobile);
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } catch (SQLException ex) {
            throw new ServletException("Registration failed due to database error", ex);
        }
    }
}
