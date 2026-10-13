package com.shopsphere.service;

import com.shopsphere.dao.ReviewDAO;
import com.shopsphere.model.Review;

import java.sql.SQLException;
import java.util.List;

/**
 * Service Layer: Product Reviews and Ratings.
 */
public class ReviewService {

    private final ReviewDAO dao = new ReviewDAO();

    public List<Review> getReviewsForProduct(int productId) throws SQLException {
        return dao.getByProductId(productId);
    }

    public boolean addReview(int userId, int productId, int rating, String text) throws SQLException {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        return dao.addReview(userId, productId, rating, text != null ? text.trim() : "");
    }

    public double getAverageRating(int productId) throws SQLException {
        return dao.getAverageRating(productId);
    }

    public int getReviewCount(int productId) throws SQLException {
        return dao.getReviewCount(productId);
    }
}
