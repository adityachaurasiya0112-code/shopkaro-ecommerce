package com.shopsphere.dao;

import com.shopsphere.model.Category;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for categories table.
 */
public class CategoryDAO {

    private Category mapCategory(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("status")
        );
    }

    public List<Category> getAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY category_id ASC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCategory(rs));
            }
        }
        return list;
    }

    public List<Category> getAllActive() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE status = 1 ORDER BY name ASC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapCategory(rs));
            }
        }
        return list;
    }

    public Category getById(int categoryId) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCategory(rs);
            }
        }
        return null;
    }

    public boolean insert(String name, String description) throws SQLException {
        String sql = "INSERT INTO categories (name, description, status) VALUES (?, ?, 1)";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(int categoryId, String name, String description, int status) throws SQLException {
        String sql = "UPDATE categories SET name = ?, description = ?, status = ? WHERE category_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, status);
            ps.setInt(4, categoryId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;
        }
    }
}
