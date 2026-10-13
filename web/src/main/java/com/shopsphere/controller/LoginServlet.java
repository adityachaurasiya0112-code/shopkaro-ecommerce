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
 * Controller for user authentication.
 * Demonstrates RTU syllabus: HttpServlet, doGet/doPost, HttpSession, session migration.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            resp.sendRedirect(req.getContextPath() + (user.isAdmin() ? "/admin/dashboard" : "/products"));
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String redirect = req.getParameter("redirect");

        try {
            User user = authService.login(email, password);
            if (user == null) {
                req.setAttribute("error", "Invalid email or password. Please try again.");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            // Create new session or migrate session ID (prevents session fixation attacks)
            HttpSession session = req.getSession(true);
            try {
                req.changeSessionId();
            } catch (Exception ignored) {}

            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            if (redirect != null && !redirect.isBlank() && !redirect.contains("login") && !redirect.contains("logout")) {
                resp.sendRedirect(redirect);
            } else if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/products");
            }
        } catch (SQLException e) {
            throw new ServletException("Login failed due to database error", e);
        }
    }
}
