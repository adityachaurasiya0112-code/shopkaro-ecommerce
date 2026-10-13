package com.shopsphere.controller;

import com.shopsphere.model.Address;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.CouponService;
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
 * Controller for Checkout and Order Placement.
 * Enforces server-side validation and price computation per RTU guidelines.
 */
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CheckoutService checkoutService = new CheckoutService();
    private final UserService userService = new UserService();
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
            if (items.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            double subtotal = items.stream().mapToDouble(CartItem::getSubtotal).sum();
            List<Address> addresses = userService.getUserAddresses(userId);

            HttpSession session = req.getSession();
            String couponCode = (String) session.getAttribute("appliedCouponCode");
            double discount = 0.0;
            Coupon coupon = null;
            if (couponCode != null && !couponCode.isBlank()) {
                coupon = couponService.validateCoupon(couponCode, subtotal);
                if (coupon != null) {
                    discount = coupon.calculateDiscount(subtotal);
                }
            }

            double total = Math.max(0.0, subtotal - discount);

            req.setAttribute("cartItems", items);
            req.setAttribute("addresses", addresses);
            req.setAttribute("subtotal", Math.round(subtotal * 100.0) / 100.0);
            req.setAttribute("discount", Math.round(discount * 100.0) / 100.0);
            req.setAttribute("total", Math.round(total * 100.0) / 100.0);
            req.setAttribute("appliedCoupon", coupon);

            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error preparing checkout", e);
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
                resp.sendRedirect(req.getContextPath() + "/checkout");
                return;
            }

            // Place Order
            String addressIdParam = req.getParameter("addressId");
            Integer addressId = null;
            if (addressIdParam != null && !addressIdParam.isBlank()) {
                addressId = Integer.parseInt(addressIdParam);
            }

            String paymentMethod = req.getParameter("paymentMethod");
            if (paymentMethod == null || paymentMethod.isBlank()) {
                paymentMethod = "CARD";
            }

            HttpSession session = req.getSession();
            String couponCode = (String) session.getAttribute("appliedCouponCode");

            int orderId = checkoutService.placeOrder(userId, addressId, couponCode, paymentMethod);

            // Clean coupon from session
            session.removeAttribute("appliedCouponCode");

            resp.sendRedirect(req.getContextPath() + "/order-success.jsp?orderId=" + orderId);
        } catch (SQLException e) {
            req.setAttribute("error", "Unable to complete order: " + e.getMessage());
            doGet(req, resp);
        }
    }
}
