package com.shopsphere.controller;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.CouponService;
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
 * Controller for Shopping Cart management.
 * Demonstrates Session tracking, Cart CRUD operations, and Coupon application.
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CheckoutService checkoutService = new CheckoutService();
    private final CouponService couponService = new CouponService();

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
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + req.getRequestURI());
            return;
        }

        try {
            List<CartItem> items = checkoutService.cart(userId);
            double subtotal = 0.0;
            for (CartItem item : items) {
                subtotal += item.getSubtotal();
            }

            // Check if coupon is stored in session
            HttpSession session = req.getSession();
            String couponCode = (String) session.getAttribute("appliedCouponCode");
            double discount = 0.0;
            Coupon appliedCoupon = null;
            if (couponCode != null && !couponCode.isBlank()) {
                appliedCoupon = couponService.validateCoupon(couponCode, subtotal);
                if (appliedCoupon != null) {
                    discount = appliedCoupon.calculateDiscount(subtotal);
                } else {
                    session.removeAttribute("appliedCouponCode");
                }
            }

            double total = Math.max(0.0, subtotal - discount);

            req.setAttribute("items", items);
            req.setAttribute("subtotal", Math.round(subtotal * 100.0) / 100.0);
            req.setAttribute("discount", Math.round(discount * 100.0) / 100.0);
            req.setAttribute("total", Math.round(total * 100.0) / 100.0);
            req.setAttribute("appliedCoupon", appliedCoupon);

            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error loading cart", e);
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
        String qtyParam = req.getParameter("quantity");
        String couponCode = req.getParameter("couponCode");

        try {
            if ("applyCoupon".equalsIgnoreCase(action)) {
                if (couponCode != null && !couponCode.isBlank()) {
                    List<CartItem> items = checkoutService.cart(userId);
                    double subtotal = items.stream().mapToDouble(CartItem::getSubtotal).sum();
                    Coupon coupon = couponService.validateCoupon(couponCode, subtotal);
                    if (coupon != null) {
                        req.getSession().setAttribute("appliedCouponCode", coupon.getCode());
                        req.getSession().setAttribute("couponSuccess", "Coupon " + coupon.getCode() + " applied successfully!");
                    } else {
                        req.getSession().setAttribute("couponError", "Invalid coupon or order minimum not reached.");
                    }
                }
            } else if ("removeCoupon".equalsIgnoreCase(action)) {
                req.getSession().removeAttribute("appliedCouponCode");
            } else if ("update".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(productIdParam);
                int quantity = Integer.parseInt(qtyParam);
                checkoutService.updateQuantity(userId, productId, quantity);
            } else if ("remove".equalsIgnoreCase(action)) {
                int productId = Integer.parseInt(productIdParam);
                checkoutService.remove(userId, productId);
            } else if ("clear".equalsIgnoreCase(action)) {
                checkoutService.clear(userId);
            } else {
                // Default: Add to cart
                int productId = Integer.parseInt(productIdParam);
                int quantity = 1;
                if (qtyParam != null && !qtyParam.isBlank()) {
                    quantity = Integer.parseInt(qtyParam);
                }
                checkoutService.add(userId, productId, quantity);
            }

            resp.sendRedirect(req.getContextPath() + "/cart");
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/cart?error=invalid_input");
        } catch (SQLException e) {
            throw new ServletException("Cart operation failed", e);
        }
    }
}
