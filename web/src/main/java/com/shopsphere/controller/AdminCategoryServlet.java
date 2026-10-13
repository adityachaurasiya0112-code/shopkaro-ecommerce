package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.service.CategoryService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Admin Category CRUD.
 */
@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {

    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Category> categories = categoryService.getAll();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/admin/categories.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error loading categories", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                categoryService.deleteCategory(id);
            } else if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                String desc = req.getParameter("description");
                int status = Integer.parseInt(req.getParameter("status"));
                categoryService.updateCategory(id, name, desc, status);
            } else {
                String name = req.getParameter("name");
                String desc = req.getParameter("description");
                categoryService.addCategory(name, desc);
            }

            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (Exception e) {
            req.getSession().setAttribute("adminError", "Operation failed: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        }
    }
}
