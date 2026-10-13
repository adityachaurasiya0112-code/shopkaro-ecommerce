package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for products table.
 * Demonstrates PreparedStatement, joins, filtering, and pagination/sorting.
 */
public class ProductDAO {

    public static Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        try {
            p.setCategoryName(rs.getString("category_name"));
        } catch (SQLException ignored) {
            // category_name may not be projected in some queries
        }
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setDiscount(rs.getDouble("discount"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getInt("status") == 1);
        try {
            p.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException ignored) {}
        return p;
    }

    public List<Product> search(String keyword, Integer categoryId, Double minPrice, Double maxPrice, String sort) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.name AS category_name FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.category_id " +
                "WHERE p.status = 1 "
        );

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (categoryId != null && categoryId > 0) {
            sql.append("AND p.category_id = ? ");
            params.add(categoryId);
        }

        if (minPrice != null && minPrice >= 0) {
            sql.append("AND p.price >= ? ");
            params.add(minPrice);
        }

        if (maxPrice != null && maxPrice > 0) {
            sql.append("AND p.price <= ? ");
            params.add(maxPrice);
        }

        if ("price_asc".equalsIgnoreCase(sort)) {
            sql.append("ORDER BY p.price ASC ");
        } else if ("price_desc".equalsIgnoreCase(sort)) {
            sql.append("ORDER BY p.price DESC ");
        } else if ("discount".equalsIgnoreCase(sort)) {
            sql.append("ORDER BY p.discount DESC ");
        } else {
            sql.append("ORDER BY p.product_id DESC ");
        }

        List<Product> list = new ArrayList<>();
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }
        }
        return list;
    }

    public List<Product> search(String keyword, String sort) throws SQLException {
        return search(keyword, null, null, null, sort);
    }

    public List<Product> getAllForAdmin() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "ORDER BY p.product_id DESC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        }
        return list;
    }

    public Product getById(int productId) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapProduct(rs);
            }
        }
        return null;
    }

    public List<Product> getFeatured(int limit) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.status = 1 ORDER BY p.discount DESC, p.product_id DESC LIMIT ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit <= 0 ? 8 : limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }
        }
        return list;
    }

    public List<Product> getLowStock(int threshold) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.status = 1 AND p.stock <= ? ORDER BY p.stock ASC";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }
        }
        return list;
    }

    public int insert(Product p) throws SQLException {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setInt(9, p.isStatus() ? 1 : 0);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void insert(String name, String brand, int categoryId, double price, double discount, int stock) throws SQLException {
        Product p = new Product();
        p.setName(name);
        p.setBrand(brand);
        p.setCategoryId(categoryId);
        p.setPrice(price);
        p.setDiscount(discount);
        p.setStock(stock);
        p.setStatus(true);
        p.setImageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500");
        insert(p);
    }

    public boolean update(Product p) throws SQLException {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, " +
                     "price = ?, discount = ?, stock = ?, image_url = ?, status = ? WHERE product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setInt(9, p.isStatus() ? 1 : 0);
            ps.setInt(10, p.getProductId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean setStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public void disable(int productId) throws SQLException {
        String sql = "UPDATE products SET status = 0 WHERE product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        }
    }

    public boolean delete(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DBUtil.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        }
    }
}
