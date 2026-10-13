package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Admin Product Management (CRUD).
 */
@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Product> products = productService.getAllForAdmin();
            List<Category> categories = categoryService.getAll();

            req.setAttribute("products", products);
            req.setAttribute("categories", categories);

            req.getRequestDispatcher("/admin/products.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error querying products", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                productService.remove(id);
            } else if ("update".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Product p = productService.getProduct(id);
                if (p != null) {
                    p.setName(req.getParameter("name"));
                    p.setBrand(req.getParameter("brand"));
                    p.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
                    p.setPrice(Double.parseDouble(req.getParameter("price")));
                    p.setDiscount(Double.parseDouble(req.getParameter("discount")));
                    p.setStock(Integer.parseInt(req.getParameter("stock")));
                    String img = req.getParameter("imageUrl");
                    if (img != null && !img.isBlank()) p.setImageUrl(img);
                    p.setDescription(req.getParameter("description"));
                    productService.update(p);
                }
            } else if ("setStock".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                int stock = Integer.parseInt(req.getParameter("stock"));
                productService.setStock(id, stock);
            } else {
                // Add new product
                Product p = new Product();
                p.setName(req.getParameter("name"));
                p.setBrand(req.getParameter("brand"));
                p.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
                p.setPrice(Double.parseDouble(req.getParameter("price")));
                String discParam = req.getParameter("discount");
                p.setDiscount(discParam != null && !discParam.isBlank() ? Double.parseDouble(discParam) : 0.0);
                p.setStock(Integer.parseInt(req.getParameter("stock")));
                String img = req.getParameter("imageUrl");
                p.setImageUrl(img != null && !img.isBlank() ? img : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500");
                p.setDescription(req.getParameter("description"));
                p.setStatus(true);
                productService.add(p);
            }

            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (Exception e) {
            req.getSession().setAttribute("adminError", "Operation failed: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        }
    }
}
