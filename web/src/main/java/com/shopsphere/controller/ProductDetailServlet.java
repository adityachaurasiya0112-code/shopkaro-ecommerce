package com.shopsphere.controller;

import com.shopsphere.model.Product;
import com.shopsphere.model.Review;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Controller for single Product details, reviews, and cookie tracking.
 * Demonstrates RTU syllabus: HttpServlet, Cookie handling for recently viewed items.
 */
@WebServlet("/product")
public class ProductDetailServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);
            Product product = productService.getProduct(productId);
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                return;
            }

            List<Review> reviews = reviewService.getReviewsForProduct(productId);
            double avgRating = reviewService.getAverageRating(productId);
            int reviewCount = reviewService.getReviewCount(productId);
            List<Product> related = productService.search(null, product.getCategoryId(), null, null, null);

            req.setAttribute("product", product);
            req.setAttribute("reviews", reviews);
            req.setAttribute("avgRating", avgRating);
            req.setAttribute("reviewCount", reviewCount);
            req.setAttribute("relatedProducts", related);

            // Cookie demonstration: track recently viewed product ID (safe non-sensitive cookie)
            updateRecentlyViewedCookie(req, resp, productId);

            req.getRequestDispatcher("/product-details.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        } catch (SQLException e) {
            throw new ServletException("Error fetching product details", e);
        }
    }

    private void updateRecentlyViewedCookie(HttpServletRequest req, HttpServletResponse resp, int currentId) {
        String existing = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("recently_viewed".equals(c.getName())) {
                    existing = c.getValue();
                    break;
                }
            }
        }

        String updated = String.valueOf(currentId);
        if (!existing.isEmpty()) {
            List<String> ids = Arrays.asList(existing.split("-"));
            if (!ids.contains(String.valueOf(currentId))) {
                updated = currentId + "-" + existing;
                // keep max 5 IDs
                String[] parts = updated.split("-");
                if (parts.length > 5) {
                    updated = String.join("-", Arrays.copyOfRange(parts, 0, 5));
                }
            } else {
                updated = existing;
            }
        }

        Cookie cookie = new Cookie("recently_viewed", updated);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        resp.addCookie(cookie);
    }
}
