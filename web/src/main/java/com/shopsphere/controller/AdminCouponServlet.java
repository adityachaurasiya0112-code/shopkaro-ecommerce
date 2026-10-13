package com.shopsphere.controller;

import com.shopsphere.model.Coupon;
import com.shopsphere.service.CouponService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Admin Coupon Management.
 */
@WebServlet("/admin/coupons")
public class AdminCouponServlet extends HttpServlet {

    private final CouponService couponService = new CouponService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Coupon> coupons = couponService.getAllCoupons();
            req.setAttribute("coupons", coupons);
            req.getRequestDispatcher("/admin/coupons.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error loading coupons", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String code = req.getParameter("code");
            String type = req.getParameter("discountType");
            double val = Double.parseDouble(req.getParameter("discountValue"));
            double min = Double.parseDouble(req.getParameter("minimumOrder"));
            double max = Double.parseDouble(req.getParameter("maximumDiscount"));
            String exp = req.getParameter("expiryDate");

            Coupon c = new Coupon();
            c.setCode(code.toUpperCase().trim());
            c.setDiscountType(type != null ? type : "PERCENT");
            c.setDiscountValue(val);
            c.setMinimumOrder(min);
            c.setMaximumDiscount(max);
            if (exp != null && !exp.isBlank()) {
                c.setExpiryDate(Date.valueOf(exp));
            } else {
                c.setExpiryDate(Date.valueOf("2030-12-31"));
            }
            c.setStatus(1);

            couponService.addCoupon(c);
            resp.sendRedirect(req.getContextPath() + "/admin/coupons");
        } catch (Exception e) {
            req.getSession().setAttribute("adminError", "Failed to create coupon: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/coupons");
        }
    }
}
