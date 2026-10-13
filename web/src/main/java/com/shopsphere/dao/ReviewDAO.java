package com.shopsphere.dao;

import com.shopsphere.model.Review;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for reviews table.
 */
public class ReviewDAO {

    public List<Review> getByProductId(int productId) throws SQLException {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, u.name AS user_name, p.name AS product_name " +
                     "FROM reviews r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "JOIN products p ON r.product_id = p.product_id " +
                     "WHERE r.product_id = ? ORDER BY r.review_id DESC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Review(
                            rs.getInt("review_id"),
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("rating"),
                            rs.getString("review_text"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return list;
    }

    public boolean addReview(int userId, int productId, int rating, String reviewText) throws SQLException {
        String sql = "INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, Math.max(1, Math.min(5, rating)));
            ps.setString(4, reviewText);
            return ps.executeUpdate() > 0;
        }
    }

    public double getAverageRating(int productId) throws SQLException {
        String sql = "SELECT AVG(rating) FROM reviews WHERE product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double avg = rs.getDouble(1);
                    return Math.round(avg * 10.0) / 10.0;
                }
            }
        }
        return 0.0;
    }

    public int getReviewCount(int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }
}
