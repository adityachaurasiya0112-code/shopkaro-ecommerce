package com.shopsphere.controller;

import com.shopsphere.model.User;
import com.shopsphere.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for Submitting Product Reviews and Ratings.
 */
@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String productIdParam = req.getParameter("productId");
        String ratingParam = req.getParameter("rating");
        String text = req.getParameter("reviewText");

        try {
            int productId = Integer.parseInt(productIdParam);
            int rating = Integer.parseInt(ratingParam);

            reviewService.addReview(user.getUserId(), productId, rating, text);
            resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "#reviews");
        } catch (NumberFormatException | SQLException e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
