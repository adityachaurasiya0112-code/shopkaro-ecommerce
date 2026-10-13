package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;

import java.sql.SQLException;

/**
 * Service Layer: Authentication and User Lifecycle.
 * Implements server-side validations per RTU security rules.
 */
public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String password) throws SQLException {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return null;
        }
        return userDAO.authenticate(email.trim().toLowerCase(), password);
    }

    public User register(String name, String email, String password, String mobile) throws SQLException {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Full name must have at least 2 characters.");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Please provide a valid email address.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        String cleanEmail = email.trim().toLowerCase();
        if (userDAO.exists(cleanEmail)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        int userId = userDAO.create(name.trim(), cleanEmail, password, "CUSTOMER", mobile != null ? mobile.trim() : null);
        return userDAO.getById(userId);
    }

    public boolean changePassword(int userId, String newPassword) throws SQLException {
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        return userDAO.updatePassword(userId, newPassword);
    }
}
