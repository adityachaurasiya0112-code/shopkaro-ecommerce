package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.model.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * Service Layer: Category operations.
 */
public class CategoryService {

    private final CategoryDAO dao = new CategoryDAO();

    public List<Category> getAll() throws SQLException {
        return dao.getAll();
    }

    public List<Category> getAllActive() throws SQLException {
        return dao.getAllActive();
    }

    public Category getById(int categoryId) throws SQLException {
        return dao.getById(categoryId);
    }

    public boolean addCategory(String name, String description) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        return dao.insert(name.trim(), description != null ? description.trim() : "");
    }

    public boolean updateCategory(int id, String name, String description, int status) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name is required.");
        }
        return dao.update(id, name.trim(), description != null ? description.trim() : "", status);
    }

    public boolean deleteCategory(int id) throws SQLException {
        return dao.delete(id);
    }
}
