package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * Service Layer: Product catalog and inventory management.
 */
public class ProductService {

    private final ProductDAO dao = new ProductDAO();

    public List<Product> list(String query, String sort) throws SQLException {
        return dao.search(query, sort);
    }

    public List<Product> search(String query, Integer categoryId, Double minPrice, Double maxPrice, String sort) throws SQLException {
        return dao.search(query, categoryId, minPrice, maxPrice, sort);
    }

    public Product getProduct(int productId) throws SQLException {
        return dao.getById(productId);
    }

    public List<Product> getFeatured(int limit) throws SQLException {
        return dao.getFeatured(limit);
    }

    public List<Product> getLowStock(int threshold) throws SQLException {
        return dao.getLowStock(threshold);
    }

    public List<Product> getAllForAdmin() throws SQLException {
        return dao.getAllForAdmin();
    }

    public void add(String name, String brand, int categoryId, double price, double discount, int stock) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0.");
        }
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }
        dao.insert(name.trim(), brand != null ? brand.trim() : "", categoryId, price, discount, stock);
    }

    public void add(Product p) throws SQLException {
        if (p == null || p.getName() == null || p.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid product details.");
        }
        dao.insert(p);
    }

    public void update(Product p) throws SQLException {
        if (p == null || p.getName() == null || p.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid product details.");
        }
        dao.update(p);
    }

    public void setStock(int productId, int stock) throws SQLException {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        dao.setStock(productId, stock);
    }

    public void remove(int productId) throws SQLException {
        dao.disable(productId);
    }

    public void deletePermanent(int productId) throws SQLException {
        dao.delete(productId);
    }
}
