package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.model.WishlistItem;
import com.shopsphere.service.WishlistService;
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
 * Controller for Customer Wishlist.
 */
@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();

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
            List<WishlistItem> items = wishlistService.getWishlist(userId);
            req.setAttribute("wishlistItems", items);
            req.getRequestDispatcher("/wishlist.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error loading wishlist", e);
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
        String productIdParam = req.getParameter("productId");

        try {
            int productId = Integer.parseInt(productIdParam);

            if ("remove".equalsIgnoreCase(action)) {
                wishlistService.removeFromWishlist(userId, productId);
            } else if ("moveToCart".equalsIgnoreCase(action)) {
                wishlistService.moveToCart(userId, productId);
            } else {
                wishlistService.addToWishlist(userId, productId);
            }

            String referer = req.getHeader("Referer");
            if (referer != null && !referer.isBlank() && !referer.contains("login")) {
                resp.sendRedirect(referer);
            } else {
                resp.sendRedirect(req.getContextPath() + "/wishlist");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/wishlist");
        } catch (SQLException e) {
            throw new ServletException("Wishlist operation failed", e);
        }
    }
}
